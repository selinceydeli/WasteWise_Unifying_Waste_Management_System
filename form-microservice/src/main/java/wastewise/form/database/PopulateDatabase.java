package wastewise.form.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;

@Service
public class PopulateDatabase {

    private final transient MongoTemplate mongoTemplate;

    @Autowired
    public PopulateDatabase(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Generates n random Form objects and insert them into the collection.
     *
     * @param n Number of forms to generate and insert.
     */
    public void generateAndInsertRandomForms(int n) {
        List<FormItem> forms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FormItem form = generateRandomForm();
            forms.add(form);
        }
        // Insert all generated documents at once into the collection
        mongoTemplate.insertAll(forms);
    }

    /**
     * Generates random Form objects.
     *
     * @return Random form item.
     */
    private FormItem generateRandomForm() {
        FormItem form = new FormItem();

        // Random data generation
        form.setDescription("Form description tells you about the problem.");
        form.setName(generateRandomName());

        Location location = new Location();
        location.setCountry(generateRandomCountry());
        location.setCity(generateRandomCity());
        location.setPostcode(generateRandomPostcode());

        form.setLocation(location);

        return form;
    }

    private String generateRandomName() {
        String[] firstNames = { "Daniel", "Charles", "Christopher",
                "Thomas", "Joseph", "Richard",
                "Patricia", "Jennifer", "Linda",
                "Elizabeth", "Barbara", "Susan",
                "Jessica", "Karen", "Sarah",
                "William", "David" };
        return firstNames[new Random().nextInt(firstNames.length)];
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
