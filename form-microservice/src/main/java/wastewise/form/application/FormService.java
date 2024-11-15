package wastewise.form.application;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;
import wastewise.form.repository.FormRepository;

/**
 * Form Service.
 */
@Service
public class FormService {

    private final transient FormRepository formRepository;
    private final transient MongoClient mongoClient;
    private final transient MongoTemplate mongoTemplate;

    /**
     * Instantiates a new Form Service.
     *
     * @param formRepository Mongo Repository to save forms at.
     * @param mongoClient    Client instance.
     * @param mongoTemplate  Template instance.
     */
    @Autowired
    public FormService(FormRepository formRepository, MongoClient mongoClient, MongoTemplate mongoTemplate) {
        this.formRepository = formRepository;
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

    public void addForm(FormItem formItem) {
        mongoTemplate.insert(formItem, "formCollection");
    }

    public List<FormItem> getFormByLocation(Location location) {
        return formRepository.findByLocation(location);
    }

    public List<FormItem> getAllForms() {
        return formRepository.findAll(); // Simpler with MongoRepository
    }

    public long getCollectionDocumentsCount(String collectionName) {
        return mongoTemplate.getCollection(collectionName).countDocuments();
    }

    /**
     * Measures performance of a given collection.
     *
     * @param collectionName Name of (non-)sharded collection.
     * @param countryName    Name of country to test performance on.
     * @return Results of test.
     */
    public String measurePerformance(String collectionName, String countryName) {
        MongoDatabase database = mongoClient.getDatabase("FormDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        long startTime = System.currentTimeMillis();

        long count = collection.countDocuments(new Document("location.country", countryName));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return "Collection: " + collectionName + ", Document Count: " + count + ", Duration: " + duration + " ms";
    }

}
