package wastewise.user.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.user.domain.Admin;
import wastewise.user.domain.Company;
import wastewise.user.domain.Location;
import wastewise.user.domain.PermissionType;

@Service
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class PopulateDatabase {

    private final transient MongoTemplate mongoTemplate;
    private static final String ADMIN_COL = "adminCollection";

    /**
     * Populate Database Service.
     *
     * @param mongoTemplate Template instance.
     */
    @Autowired
    public PopulateDatabase(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Generates and inserts random users.
     *
     * @param n          Number of users to insert.
     * @param collection Collection to insert to.
     */
    public void generateAndInsertRandomUsers(int n, String collection) {
        if (collection.equals(ADMIN_COL)) {
            List<Admin> admins = new ArrayList<Admin>();

            for (int i = 0; i < n; i++) {
                admins.add(adminGenerator());
            }

            mongoTemplate.insert(admins, collection);
        } else {
            List<Company> companies = new ArrayList<Company>();

            for (int i = 0; i < n; i++) {
                companies.add(companyGenerator());
            }

            mongoTemplate.insert(companies, collection);
        }
    }

    private Admin adminGenerator() {
        PermissionType[] values = PermissionType.values();
        Random random = new Random();

        return new Admin(generateRandomEmail(),
                generateRandomString(15),
                new ArrayList<>(),
                values[random.nextInt(values.length)]);
    }

    private Company companyGenerator() {
        return new Company(generateRandomEmail(),
                generateRandomString(15),
                generateCompanyName(),
                new Date(),
                generateRandomString(20),
                new ArrayList<>(),
                generateRandomLocation());
    }

    private Location generateRandomLocation() {
        String[] countries = { "Spain", "UK", "Netherlands", "Poland", "Romania", "Turkey", "Germany", "Sweden",
                "Norway", "France" };
        String[] cities = { "Madrid", "London", "Amsterdam", "Warsaw", "Bucharest", "Ankara", "Berlin", "Stockholm",
                "Oslo", "Paris" };
        Random rand = new Random();

        return new Location(countries[rand.nextInt(countries.length)], cities[rand.nextInt(cities.length)]);
    }

    private String generateCompanyName() {
        String[] companies = { "Innova", "Nebula", "Solace", "Vertex", "Nimbus", "Quantum", "Aether", "Pioneer",
                "Stratos", "Vanguard" };
        Random rand = new Random();

        return companies[rand.nextInt(companies.length)];
    }

    private String generateRandomEmail() {
        String[] firstNames = { "Daniel", "Charles", "Christopher",
                "Thomas", "Joseph", "Richard",
                "Patricia", "Jennifer", "Linda",
                "Elizabeth", "Barbara", "Susan",
                "Jessica", "Karen", "Sarah",
                "William", "David" };
        return firstNames[new Random().nextInt(firstNames.length)] + "@"
                + generateRandomString(10) + ".nl";
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
}
