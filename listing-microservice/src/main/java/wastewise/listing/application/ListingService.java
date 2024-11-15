package wastewise.listing.application;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import wastewise.listing.domain.Listing;
import wastewise.listing.repositories.ListingRepository;

@Service
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class ListingService {

    private final transient ListingRepository listingRepository;
    private final transient MongoClient mongoClient;
    private final transient MongoTemplate mongoTemplate;
    private transient Random random = new Random();

    /**
     * Instantiates a new Listing Service.
     *
     * @param listingRepository Repository to link listings to.
     * @param mongoTemplate     Template instance.
     * @param mongoClient       Client instance.
     */
    @Autowired
    public ListingService(ListingRepository listingRepository, MongoTemplate mongoTemplate, MongoClient mongoClient) {
        this.listingRepository = listingRepository;
        this.mongoTemplate = mongoTemplate;
        this.mongoClient = mongoClient;
    }

    public long getCountOfListings() {
        return listingRepository.count();
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll(); // Simpler with MongoRepository
    }

    public void addListing(Listing listing) {
        listingRepository.save(listing); // Using save method from MongoRepository
    }

    public List<Listing> getListingsByCompanyName(String companyName) {
        return listingRepository.findByCompanyName(companyName);
    }

    public List<Listing> getListingsByMaterial(String material) {
        return listingRepository.findByMaterial(material);
    }

    public List<Listing> getListingsByLocation(String location) {
        return listingRepository.findByLocation(location);
    }

    public long getCollectionDocumentsCount(String collectionName) {
        return mongoTemplate.getCollection(collectionName).countDocuments();
    }

    /**
     * Measures querying performance of a given collection.
     *
     * @param collectionName Name of (non-)sharded collection.
     * @param countryName    Name of country to test performance on.
     * @return Results of test.
     */
    public String measurePerformance(String collectionName, String countryName) {
        MongoDatabase database = mongoClient.getDatabase("ListingDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        long startTime = System.currentTimeMillis();
        long count = collection.countDocuments(new Document("location", countryName));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return "Collection: " + collectionName + ", Document Count: " + count + ", Duration: " + duration + " ms";
    }

    /**
     * Measures throughput of querying for given collection.
     *
     * @param collectionName    Name of (non-)sharded collection.
     * @param durationInSeconds Time over which to test throughput.
     * @return Results of test.
     */
    public String measureThroughput(String collectionName, int durationInSeconds) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationInSeconds * 1000;
        List<String> locations = Arrays.asList(
                "Albania", "Andorra", "Armenia", "Austria", "Azerbaijan",
                "Belarus", "Belgium", "Bosnia and Herzegovina", "Bulgaria", "Croatia",
                "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland",
                "France", "Georgia", "Germany", "Greece", "Hungary",
                "Iceland", "Ireland", "Italy", "Kosovo", "Latvia",
                "Lithuania", "Luxembourg", "Malta", "Moldova", "Monaco",
                "Montenegro", "Netherlands", "North Macedonia", "Norway", "Poland",
                "Portugal", "Romania", "San Marino", "Serbia", "Slovakia",
                "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey",
                "Ukraine", "United Kingdom", "Vatican City");
        int operationsCount = 0;
        while (System.currentTimeMillis() < endTime) {
            String location = locations.get(random.nextInt(locations.size())); // Randomly pick the location
            mongoTemplate.find(Query.query(Criteria.where("location").is(location)), Listing.class, collectionName);
            operationsCount++;
        }

        double throughput = operationsCount / (double) durationInSeconds;
        return "Throughput for " + collectionName + ": " + throughput + " operations/sec";
    }

    /**
     * Simulates high frequency querying scenarios.
     *
     * @param collectionName Name of (non-)sharded collection.
     * @param location       Location to query for.
     * @param queryCount     Number of queries to conduct.
     * @return Results of experimentation.
     */
    public String simulateHighFrequencyQuerying(String collectionName, String location, int queryCount) {
        MongoDatabase database = mongoClient.getDatabase("ListingDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        long totalDuration = 0;
        int successfulQueries = 0;

        for (int i = 0; i < queryCount; i++) {
            long startTime = System.currentTimeMillis();
            long count = collection.countDocuments(new Document("location", location));
            long endTime = System.currentTimeMillis();

            long duration = endTime - startTime;
            totalDuration += duration;
            if (count > 0) {
                successfulQueries++;
            }
        }

        double averageLatency = successfulQueries > 0 ? totalDuration / (double) successfulQueries : 0;
        return "Collection: " + collectionName + ", Location: " + location + ", Total Queries: " + queryCount
                + ", Successful Queries: " + successfulQueries + ", Average Latency: " + averageLatency + " ms/query";
    }
}
