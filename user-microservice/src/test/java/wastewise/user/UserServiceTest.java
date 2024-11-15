package wastewise.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.user.application.UserService;
import wastewise.user.domain.Admin;
import wastewise.user.domain.Company;
import wastewise.user.domain.Location;
import wastewise.user.domain.PermissionType;
import wastewise.user.repositories.AdminRepository;
import wastewise.user.repositories.CompanyRepository;

public class UserServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private UserService userService;

    private Admin admin;
    private Company company;
    private Location location;
    private List<Location> locationList;

    /**
     * Sets up environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        locationList = new ArrayList<>();
        location = new Location();
        locationList.add(location);
        admin = new Admin("admin@example.com", "password", locationList, PermissionType.PERMISSION_1);
        company = new Company("company@example.com", "password", "Company Name",
                new Date(), "Company Description", new ArrayList<>(), new Location());
    }

    @Test
    public void testGetAdminByEmail() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(admin);

        Admin foundAdmin = userService.getAdminByEmail("admin@example.com");

        assertNotNull(foundAdmin);
        assertEquals("admin@example.com", foundAdmin.getEmail());
    }

    @Test
    public void testGetAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        admins.add(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> allAdmins = userService.getAllAdmins();

        assertEquals(1, allAdmins.size());
        assertEquals("admin@example.com", allAdmins.get(0).getEmail());
    }

    @Test
    public void testCreateLowerLevelAdmin_Success() {
        Admin upperAdmin = new Admin("upperAdmin@example.com", "password",
                List.of(new Location()), PermissionType.PERMISSION_2);
        when(adminRepository.findByEmail("upperAdmin@example.com")).thenReturn(upperAdmin);

        String result = userService.createLowerLevelAdmin(upperAdmin, "newAdmin@example.com",
                new ArrayList<>(), PermissionType.PERMISSION_1);

        assertEquals("Success", result);
        verify(mongoTemplate).insert(any(Admin.class), eq("adminCollection"));
    }

    @Test
    public void testCreateCompany_Fail() {
        when(companyRepository.findByEmail("admin@example.com")).thenReturn(company);

        String result = userService.createCompany(admin, "company@example.com", "Company Name",
                "Company Description", new ArrayList<>(), new Location());

        assertEquals("Permission denied. Company is not located in a city under your juristiction.", result);
    }

    @Test
    public void testCreateCompany_Success() {
        when(companyRepository.findByEmail("admin@example.com")).thenReturn(company);

        String result = userService.createCompany(admin, "company@example.com", "Company Name",
                "Company Description", new ArrayList<>(), location);

        assertEquals("Success", result);
        verify(mongoTemplate).insert(any(Company.class), eq("companyCollection"));
    }

    @Test
    public void testGetUserPermission() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(admin);

        PermissionType permission = userService.getUserPermission("admin@example.com");

        assertEquals(PermissionType.PERMISSION_1, permission);
    }


    @Test
    public void testMeasurePerformance() {
        String collectionName = "someCollection";
        String countryName = "Country A";
        MongoDatabase database = mock(MongoDatabase.class);
        MongoCollection<Document> collection = mock(MongoCollection.class);
        when(mongoClient.getDatabase("UserDB")).thenReturn(database);
        when(database.getCollection(collectionName)).thenReturn(collection);
        when(collection.countDocuments(any(Document.class))).thenReturn(5L);

        String result = userService.measurePerformance(collectionName, countryName);

        assertEquals("Collection: someCollection, Document Count: 5, Duration: ",
                result.substring(0, result.indexOf(" ms") - 1));
        verify(collection).countDocuments(new Document("email", countryName));
    }
}