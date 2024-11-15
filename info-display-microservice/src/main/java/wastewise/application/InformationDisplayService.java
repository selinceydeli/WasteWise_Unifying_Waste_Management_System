package wastewise.application;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;
import wastewise.repositories.InformationDisplayRepository;

@Service
public class InformationDisplayService {

    private final transient InformationDisplayRepository infoDisplayRepository;
    private final transient MongoClient mongoClient;
    private final transient MongoTemplate mongoTemplate;

    /**
     * Instantiates a Information Display Service.
     *
     * @param infoDisplayRepository Repository used.
     * @param mongoClient           Client instance.
     * @param mongoTemplate         Template instance.
     */
    @Autowired
    public InformationDisplayService(InformationDisplayRepository infoDisplayRepository, MongoClient mongoClient,
            MongoTemplate mongoTemplate) {
        this.infoDisplayRepository = infoDisplayRepository;
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

    public void addInformationDisplay(InformationDisplay informationDisplay) {
        mongoTemplate.insert(informationDisplay, "infoDisplay");
    }

    public List<InformationDisplay> getInfoDisplaysByLocation(Location location) {
        return infoDisplayRepository.findByLocation(location);
    }

    public List<InformationDisplay> getAllInformationDisplays() {
        return infoDisplayRepository.findAll();
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
        MongoDatabase database = mongoClient.getDatabase("InformationDisplayDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        long startTime = System.currentTimeMillis();

        long count = collection.countDocuments(new Document("location.country", countryName));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return "Collection: " + collectionName + ", Document Count: " + count + ", Duration: " + duration + " ms";
    }

}
