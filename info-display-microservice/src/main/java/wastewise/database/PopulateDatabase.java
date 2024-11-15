package wastewise.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;

@Service
public class PopulateDatabase {

    private final transient MongoTemplate mongoTemplate;
    private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final transient Random random = new Random();

    @Autowired
    public PopulateDatabase(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Method to generate n random Information Displays and insert them into the
     * collection.
     *
     * @param n Number of information displays to generate and insert.
     */
    public void generateAndInsertRandomDocuments(int n) {
        List<InformationDisplay> infoDisplays = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            InformationDisplay infoDisplay = generateRandomInformationDisplay();
            infoDisplays.add(infoDisplay);
        }
        // Insert all generated documents at once into the collection
        mongoTemplate.insertAll(infoDisplays);
    }

    /**
     * Generates random Information Display objects.
     *
     * @return Random Information Display item.
     */
    private InformationDisplay generateRandomInformationDisplay() {
        InformationDisplay infoDisplay = new InformationDisplay();

        // Random data generation
        infoDisplay.setGeneralInfo(generateRandomString(10));
        infoDisplay.setBinLocations(generateRandomString(15));
        infoDisplay.setPickupSchedule(generateRandomString(20));

        // Generate random Location object
        Location location = new Location();
        location.setCountry(generateRandomCountry());
        location.setCity(generateRandomCity());
        location.setPostcode(generateRandomPostcode());

        infoDisplay.setLocation(location);

        return infoDisplay;
    }

    // Helper methods to generate random strings and data
    private String generateRandomString(int length) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }

        return result.toString();
    }

    private String generateRandomCountry() {
        String[] countries = { "USA", "UK", "Canada", "Germany", "France" };
        return countries[new Random().nextInt(countries.length)];
    }

    private String generateRandomCity() {
        String[] cities = { "New York", "London", "Berlin", "Toronto", "Paris" };
        return cities[new Random().nextInt(cities.length)];
    }

    private String generateRandomPostcode() {
        // Random 5-digit number
        return String.valueOf(10000 + new Random().nextInt(90000));
    }
}
