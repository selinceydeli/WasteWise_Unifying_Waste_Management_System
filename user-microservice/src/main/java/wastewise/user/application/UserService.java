package wastewise.user.application;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.user.domain.Admin;
import wastewise.user.domain.Company;
import wastewise.user.domain.Location;
import wastewise.user.domain.MaterialType;
import wastewise.user.domain.PermissionType;
import wastewise.user.repositories.AdminRepository;
import wastewise.user.repositories.CompanyRepository;

@Service
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class UserService {
    private final transient AdminRepository adminRepository;
    private final transient CompanyRepository companyRepository;
    private final transient MongoClient mongoClient;
    private final transient MongoTemplate mongoTemplate;

    /**
     * Instantiates a new User Service.
     *
     * @param adminRepository   Repository that will keep admin data.
     * @param companyRepository Repository that will keep company data.
     * @param mongoClient       Client instance.
     * @param mongoTemplate     Template instance.
     */
    @Autowired
    public UserService(AdminRepository adminRepository, CompanyRepository companyRepository, MongoClient mongoClient,
            MongoTemplate mongoTemplate) {
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

    private void addAdmin(Admin admin) {
        mongoTemplate.insert(admin, "adminCollection");
    }

    private void addCompany(Company company) {
        mongoTemplate.insert(company, "companyCollection");
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public long getCollectionDocumentsCount(String collectionName) {
        return mongoTemplate.getCollection(collectionName).countDocuments();
    }

    /**
     * Measures querying performance of a given collection.
     *
     * @param collectionName Name of (non-)sharded collection.
     * @param email          Email to search for user on.
     * @return Results of test.
     */
    public String measurePerformance(String collectionName, String email) {
        MongoDatabase database = mongoClient.getDatabase("UserDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        long startTime = System.currentTimeMillis();

        long count = collection.countDocuments(new Document("email", email));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return "Collection: " + collectionName + ", Document Count: " + count + ", Duration: " + duration + " ms";
    }

    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Company findCompanyByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    /**
     * Generates a random password.
     *
     * @param length Length of password to be created.
     * @return Password as string.
     */
    private String generateRandomPassword(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            password.append(charSet.charAt(randomIndex));
        }

        return password.toString();
    }

    /**
     * Creates a new Admin of less capabilities than the creating Admin.
     *
     * @param upperLevelAdmin Admin carrying out creation.
     * @param email           Email for the new admin.
     * @param locationList    List of locations that new admin will be supervising.
     * @param permissionType  Permission level of new admin.
     * @return Response of creation request.
     */
    public String createLowerLevelAdmin(Admin upperLevelAdmin, String email, List<Location> locationList,
            PermissionType permissionType) {
        // Upper admin assigns a password to the lower admin
        String pwd = generateRandomPassword(10);
        Admin newAdmin = new Admin(email, pwd, locationList, permissionType);

        if (upperLevelAdmin.canCreateLowerLevelAdmin(newAdmin)) {
            boolean underJuristiction = upperLevelAdmin.getCities().containsAll(locationList);

            if (underJuristiction) {
                addAdmin(newAdmin);
                System.out.println("New admin created for email account " + email + " with permission level "
                        + permissionType.getLevel() + ".");
                return "Success";
            } else {
                System.out.println(
                        "Permission denied. Requested to create admin for cities not under your juristiction.");
                return "Permission denied. Requested to create admin for cities not under your juristiction.";
            }
        } else {
            System.out.println("Permission denied. Cannot create admin with higher or equal permission.");
            return "Permission denied. Cannot create admin with higher or equal permission.";
        }
    }

    /**
     * Creates a new Company account.
     *
     * @param admin        Admin handling creation.
     * @param email        Email of new Company.
     * @param name         Name of new Company.
     * @param description  Description of new Company.
     * @param listMaterial List of materials that new Company processes.
     * @param location     Location of new Company.
     * @return Response of creation request.
     */
    public String createCompany(Admin admin, String email, String name, String description,
            List<MaterialType> listMaterial, Location location) {
        boolean underJuristiction = admin.getCities().contains(location);

        if (underJuristiction) {
            // Admin with permission type 1 (i.e. lowest level admin) assigns a password to
            // Company user
            String pwd = generateRandomPassword(10);
            Company newCompany = new Company(email, pwd, name, new Date(), description, listMaterial, location);
            addCompany(newCompany);
            System.out.println("New company created: " + name + ", email: " + email);
            return "Success";
        } else {
            System.out.println("Permission denied. Company is not located in a city under your juristiction.");
            return "Permission denied. Company is not located in a city under your juristiction.";
        }
    }

    /**
     * Returns the permissions of the user for the given email.
     *
     * @param email Email to search on.
     * @return Permission type of user (if found).
     */
    public PermissionType getUserPermission(String email) {
        Admin admin = findAdminByEmail(email);

        if (admin != null) {
            return admin.getPermissionType();
        }
        return null;
    }
}
