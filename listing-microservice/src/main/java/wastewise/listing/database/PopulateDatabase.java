package wastewise.listing.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.listing.domain.Listing;
import wastewise.listing.domain.MaterialType;
import wastewise.listing.domain.Type;

@Service
public class PopulateDatabase {

    private final transient MongoTemplate mongoTemplate;

    @Autowired
    public PopulateDatabase(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Method to create a collection if it does not exist.
     *
     * @param collectionName Name of the new collection.
     */
    public void createCollectionIfNotExists(String collectionName) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
            System.out.println("Collection created: " + collectionName);
        } else {
            System.out.println("Collection already exists: " + collectionName);
        }
    }

    /**
     * Inserts listings to the collection.
     *
     * @param listings       List of Listings to insert.
     * @param collectionName Collection to insert into.
     */
    public void insertListingsIntoCollection(List<Listing> listings, String collectionName) {
        mongoTemplate.insert(listings, collectionName);
    }

    /**
     * Generate n random listings.
     *
     * @param n Number of listings to generate.
     * @return List of generated listings.
     */
    public List<Listing> generateRandomListings(int n) {
        List<Listing> listings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Listing listing = generateRandomListing();
            listings.add(listing);
        }
        return listings;
    }

    /**
     * Method to generate random Listing objects.
     *
     * @return Random listing.
     */
    private Listing generateRandomListing() {
        Listing listing = new Listing();

        // Random data generation
        listing.setType(generateRandomType());
        listing.setCompanyName(generateRandomCompanyName());
        listing.setCompanyEmail(generateRandomCompanyEmail());
        listing.setMaterial(generateRandomMaterialType());
        listing.setLocation(generateRandomLocation());
        listing.setQuantity(generateRandomQuantity());
        listing.setStart(generateRandomDate());
        listing.setEnd(generateRandomDate());

        return listing;
    }

    // Helper methods to generate random data
    private Type generateRandomType() {
        Type[] types = Type.values();
        return types[new Random().nextInt(types.length)];
    }

    private String generateRandomCompanyName() {
        String[] companies = { "CompanyA", "CompanyB", "CompanyC", "CompanyD", "CompanyE" };
        return companies[new Random().nextInt(companies.length)];
    }

    private String generateRandomCompanyEmail() {
        String[] emails = { "info@companya.com", "info@companyb.com", "info@companyc.com", "info@companyd.com",
                "info@companye.com" };
        return emails[new Random().nextInt(emails.length)];
    }

    private MaterialType generateRandomMaterialType() {
        MaterialType[] materials = MaterialType.values();
        return materials[new Random().nextInt(materials.length)];
    }

    private String generateRandomLocation() {
        String[] locations = {
                "Albania", "Andorra", "Armenia", "Austria", "Azerbaijan",
                "Belarus", "Belgium", "Bosnia and Herzegovina", "Bulgaria", "Croatia",
                "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland",
                "France", "Georgia", "Germany", "Greece", "Hungary",
                "Iceland", "Ireland", "Italy", "Kosovo", "Latvia",
                "Lithuania", "Luxembourg", "Malta", "Moldova", "Monaco",
                "Montenegro", "Netherlands", "North Macedonia", "Norway", "Poland",
                "Portugal", "Romania", "San Marino", "Serbia", "Slovakia",
                "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey",
                "Ukraine", "United Kingdom", "Vatican City"
        };
        return locations[new Random().nextInt(locations.length)];
    }

    private int generateRandomQuantity() {
        return 100 + new Random().nextInt(900); // Random quantity between 100 and 1000
    }

    private String generateRandomDate() {
        return "2024-01-" + (10 + new Random().nextInt(20)); // Random date in January 2024
    }
}
