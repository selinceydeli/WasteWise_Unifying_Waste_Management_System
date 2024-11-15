package wastewise.user.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wastewise.user.application.UserService;
import wastewise.user.database.PopulateDatabase;
import wastewise.user.domain.*;

/**
 * Controller for the User Microservice.
 */
@RestController
public class UserController {

    private final transient UserService userService;
    private final transient PopulateDatabase populateDatabase;

    /**
     * Instantiates new User Controller.
     *
     * @param userService      User Service instance.
     * @param populateDatabase Database populator.
     */
    @Autowired
    public UserController(UserService userService, PopulateDatabase populateDatabase) {
        this.userService = userService;
        this.populateDatabase = populateDatabase;
    }

    /**
     * Creates new Admin.
     *
     * @param request Admin creation request.
     * @return Response entity.
     */
    @PostMapping("/admin/create")
    public ResponseEntity<String> createLowerLevelAdmin(@RequestBody AdminCreationRequest request) {
        Admin upperAdmin = userService.findAdminByEmail(request.getUpperAdminEmail());

        if (upperAdmin != null) {
            // Attempt to create the lower-level admin
            String newEmail = request.getEmail();
            List<Location> newLocations = request.getLocations();
            PermissionType newPermission = request.getPermissionType();
            String result = userService.createLowerLevelAdmin(upperAdmin, newEmail, newLocations, newPermission);

            if (result.equals("Success")) {
                return ResponseEntity.ok("New admin created for email account " + newEmail
                        + " with permission level " + newPermission.getLevel()
                        + " with juristiction over: " + newLocations.toString() + ".");
            } else {
                return ResponseEntity.status(403).body(result);
            }
        }

        return ResponseEntity.badRequest().body("Upper-level admin not found.");
    }

    /**
     * Creates new Company.
     *
     * @param request Company creation request.
     * @return Response entity.
     */
    @PostMapping("/company/create")
    public ResponseEntity<String> createCompany(@RequestBody CompanyCreationRequest request) {
        Admin admin = userService.findAdminByEmail(request.getAdminEmail());

        if (admin != null) {
            String result = userService.createCompany(admin, request.getEmail(), request.getName(),
                    request.getDescription(),
                    request.getListMaterial(), request.getLocation());

            if (result.equals("Success")) {
                return ResponseEntity.ok("New company created with email: " + request.getEmail());
            } else {
                return ResponseEntity.status(403).body(result);
            }
        }

        return ResponseEntity.badRequest().body("Admin not found.");
    }

    /**
     * Returns all Admins in database.
     *
     * @return List of all Admins.
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    /**
     * Returns all Companies in database.
     *
     * @return List of all Companies.
     */
    @GetMapping("/company/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = userService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    /**
     * Fetches Admin by given email.
     *
     * @param email Email to search for.
     * @return Admin (if found).
     */
    @GetMapping("/admin/profile/search")
    public ResponseEntity<?> searchAdminByEmail(@RequestParam String email) {
        Admin admin = userService.findAdminByEmail(email);
        if (admin != null) {
            return ResponseEntity.ok(admin); // Return admin if found
        }
        return ResponseEntity.status(404).body("Admin not found"); // Return 404 with message
    }

    /**
     * Fetches Company by given email.
     *
     * @param email Email to search for.
     * @return Company (if found).
     */
    @GetMapping("/company/profile/search")
    public ResponseEntity<?> searchCompanyByEmail(@RequestParam String email) {
        Company company = userService.findCompanyByEmail(email);
        if (company != null) {
            return ResponseEntity.ok(company); // Return company if found
        }
        return ResponseEntity.status(404).body("Company not found"); // Return 404 with message
    }

    /**
     * Returns the user's permissions.
     *
     * @param email Email by which to find user.
     * @return Permissions (if found).
     */
    @GetMapping("/user/permission")
    public ResponseEntity<String> getUserPermission(@RequestParam String email) {
        PermissionType permissionType = userService.getUserPermission(email);

        if (permissionType != null) {
            return ResponseEntity.ok("User permission: " + permissionType);
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    /**
     * Populates Admin Collection with n Admins.
     *
     * @param n Number of Admins to populate with.
     * @return Response entity.
     */
    @PostMapping("/admin/populate")
    public ResponseEntity<String> populateAdminCollection(@RequestParam int n) {
        return populateCollection(n, "adminCollection");
    }

    /**
     * Populates Company Collection with n Companies.
     *
     * @param n Number of Companies to populate with.
     * @return Response entity.
     */
    @PostMapping("/company/populate")
    public ResponseEntity<String> populateCompanyCollection(@RequestParam int n) {
        return populateCollection(n, "companyCollection");
    }

    /**
     * Helps the population endpoints.
     *
     * @param n          Number of Users.
     * @param collection Collection to populate.
     * @return Response entity.
     */
    private ResponseEntity<String> populateCollection(int n, String collection) {
        long documentCountBefore = userService.getCollectionDocumentsCount(collection);
        System.out.println("countBefore = " + documentCountBefore);
        populateDatabase.generateAndInsertRandomUsers(n, collection);
        long documentCountAfter = userService.getCollectionDocumentsCount(collection);
        System.out.println("countAfter = " + documentCountAfter);
        if (documentCountAfter - documentCountBefore == n) {
            return ResponseEntity.ok(n + " documents inserted successfully!");
        } else {
            return ResponseEntity.ok("uh oh!!!");
        }
    }
}
