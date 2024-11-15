package wastewise.user;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wastewise.user.application.UserService;
import wastewise.user.controllers.UserController;
import wastewise.user.database.PopulateDatabase;
import wastewise.user.domain.Admin;
import wastewise.user.domain.AdminCreationRequest;
import wastewise.user.domain.Company;
import wastewise.user.domain.CompanyCreationRequest;
import wastewise.user.domain.Location;
import wastewise.user.domain.PermissionType;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PopulateDatabase populateDatabase;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private Admin admin;
    private Company company;

    /**
     * Sets up environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        admin = new Admin("admin@example.com", "password", new ArrayList<>(), PermissionType.PERMISSION_2);
        company = new Company("company@example.com", "password", "Company Name",
                new Date(), "Company Description", new ArrayList<>(), new Location());
    }

    @Test
    public void testCreateLowerLevelAdmin_Success() throws Exception {
        AdminCreationRequest request = new AdminCreationRequest();
        request.setEmail("newAdmin@example.com");
        request.setUpperAdminEmail("upperAdmin@example.com");
        request.setLocations(new ArrayList<>());
        request.setPermissionType(PermissionType.PERMISSION_1);
        when(userService.findAdminByEmail("upperAdmin@example.com")).thenReturn(admin);
        when(userService.createLowerLevelAdmin(any(Admin.class), eq("newAdmin@example.com"),
                anyList(), eq(PermissionType.PERMISSION_1))).thenReturn("Success");

        mockMvc.perform(post("/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"upperAdminEmail\":\"upperAdmin@example.com\", \"email\":\"newAdmin@example.com\", "
                                + "\"locations\":[], \"permissionType\":\"PERMISSION_1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New admin created "
                        + "for email account newAdmin@example.com")));
    }

    @Test
    public void testCreateCompany_Success() throws Exception {
        CompanyCreationRequest request = new CompanyCreationRequest();
        request.setEmail("company@example.com");
        request.setAdminEmail("admin@example.com");
        request.setName("Company Name");
        request.setDescription("Company Description");
        request.setListMaterial(new ArrayList<>());
        request.setLocation(new Location());
        when(userService.findAdminByEmail("admin@example.com")).thenReturn(admin);
        when(userService.createCompany(any(Admin.class), eq("company@example.com"), eq("Company Name"),
                eq("Company Description"), anyList(), any(Location.class))).thenReturn("Success");

        mockMvc.perform(post("/company/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"adminEmail\":\"admin@example.com\", \"email\":\"company@example.com\", "
                                + "\"name\":\"Company Name\", \"description\":\"Company Description\", "
                                + "\"listMaterial\":[], \"location\":{}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New company created "
                        + "with email: company@example.com")));
    }

    @Test
    public void testGetAllAdmins() throws Exception {
        List<Admin> admins = List.of(admin);
        when(userService.getAllAdmins()).thenReturn(admins);

        mockMvc.perform(get("/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("admin@example.com"));
    }

    @Test
    public void testGetAllCompanies() throws Exception {
        List<Company> companies = List.of(company);
        when(userService.getAllCompanies()).thenReturn(companies);

        mockMvc.perform(get("/company/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("company@example.com"));
    }

    @Test
    public void testSearchAdminByEmail_Success() throws Exception {
        when(userService.findAdminByEmail("admin@example.com")).thenReturn(admin);

        mockMvc.perform(get("/admin/profile/search?email=admin@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    public void testSearchAdminByEmail_NotFound() throws Exception {
        when(userService.findAdminByEmail("unknown@example.com")).thenReturn(null);

        mockMvc.perform(get("/admin/profile/search?email=unknown@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Admin not found"));
    }

    @Test
    public void testGetUserPermission() throws Exception {
        when(userService.getUserPermission("admin@example.com")).thenReturn(PermissionType.PERMISSION_1);

        mockMvc.perform(get("/user/permission?email=admin@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User permission: PERMISSION_1")));
    }

    @Test
    public void testPopulateAdminCollection_Fail() throws Exception {
        when(userService.getCollectionDocumentsCount("adminCollection")).thenReturn(5L);

        mockMvc.perform(post("/admin/populate?n=5"))
                .andExpect(status().isOk())
                .andExpect(content().string("uh oh!!!"));
    }
}