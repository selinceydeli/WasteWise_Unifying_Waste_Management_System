package wastewise.listing.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wastewise.listing.application.ListingService;
import wastewise.listing.database.PopulateDatabase;
import wastewise.listing.domain.Listing;

@RestController
@RequestMapping("/listings")
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class ListingsController {

    private final transient ListingService listingService;
    private final transient PopulateDatabase populateDatabase;
    private final transient MongoTemplate mongoTemplate;
    private static final String SHARDED = "listingCollection";
    private static final String NONSHARDED = "nonShardedCollection";

    /**
     * Instantiates a new Listings Controller.
     *
     * @param listingService   Service to retrieve listings.
     * @param populateDatabase Database populator.
     * @param mongoTemplate    Template instance.
     */
    @Autowired
    public ListingsController(ListingService listingService,
            PopulateDatabase populateDatabase, MongoTemplate mongoTemplate) {
        this.listingService = listingService;
        this.populateDatabase = populateDatabase;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Listing>> viewAllListings() {
        List<Listing> listings = listingService.getAllListings();
        return ResponseEntity.ok(listings);
    }

    @PostMapping("/count")
    public ResponseEntity<Long> getCountOfListings() {
        long count = listingService.getCountOfListings();
        return ResponseEntity.ok(count);
    }

    /**
     * Returns a list of Listings corresponding to the given location.
     *
     * @param location Location to fetch listings for.
     * @return List of Listings.
     */
    @GetMapping("/by-location")
    public ResponseEntity<List<Listing>> viewListingsByLocation(@RequestParam String location) {
        List<Listing> listings = listingService.getListingsByLocation(location);
        return ResponseEntity.ok(listings);
    }

    /**
     * Adds a new listing.
     *
     * @param listing Listing to be added.
     * @return Response entity.
     */
    @PostMapping
    public ResponseEntity<String> addListing(@RequestBody Listing listing) {
        listingService.addListing(listing);
        return ResponseEntity.ok("Listing added successfully.");
    }

    /**
     * Returns a list of Listings associated with the given company.
     *
     * @param companyName Name of company to search for.
     * @return List of Listings.
     */
    @GetMapping("/by-company")
    public ResponseEntity<List<Listing>> getListingsByCompanyName(@RequestParam String companyName) {
        List<Listing> listings = listingService.getListingsByCompanyName(companyName);
        if (listings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listings);
    }

    /**
     * Returns a list of Listings with the given material type.
     *
     * @param material Material type to search for.
     * @return List of listings with given material type.
     */
    @GetMapping("/by-material")
    public ResponseEntity<List<Listing>> getListingsByMaterial(@RequestParam String material) {
        List<Listing> listings = listingService.getListingsByMaterial(material);
        if (listings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listings);
    }

    /**
     * Creates a non-sharded collection in the database.
     *
     * @param collectionName Name of collection to be created.
     * @return Response entity.
     */
    @PostMapping("/createCollection")
    public ResponseEntity<String> createCollection(@RequestParam String collectionName) {
        boolean collectionExists = mongoTemplate.collectionExists(collectionName);
        if (!collectionExists) {
            populateDatabase.createCollectionIfNotExists(collectionName);
            return ResponseEntity.ok("Collection created successfully: " + collectionName);
        } else {
            return ResponseEntity.ok("Collection already exists: " + collectionName);
        }
    }

    /**
     * Populates a collection for testing purposes.
     *
     * @param n Population count.
     * @return Response message.
     */
    @PostMapping("/populate")
    public ResponseEntity<String> populateCollections(
            @RequestParam String[] collectionNames, @RequestParam int n) {

        StringBuilder resultMessage = new StringBuilder();
        boolean allSuccessful = true;

        // Generate listings
        List<Listing> listings = populateDatabase.generateRandomListings(n);

        for (String collectionName : collectionNames) {
            long documentCountBefore = listingService.getCollectionDocumentsCount(collectionName);
            System.out.println("countBefore for " + collectionName + " = " + documentCountBefore);

            // Insert the same listings to each collection
            populateDatabase.insertListingsIntoCollection(listings, collectionName);

            long documentCountAfter = listingService.getCollectionDocumentsCount(collectionName);
            System.out.println("countAfter for " + collectionName + " = " + documentCountAfter);

            if (documentCountAfter - documentCountBefore == n) {
                resultMessage.append(n).append(" documents inserted successfully into ")
                        .append(collectionName).append("!\n");
            } else {
                resultMessage.append("Failed to insert ").append(n).append(" documents into ")
                        .append(collectionName).append("!\n");
                allSuccessful = false;
            }
        }

        return ResponseEntity.ok(allSuccessful ? resultMessage.toString() : "Some collections failed!");
    }

    /**
     * Get Latency results for collections (sharded versus non-sharded).
     * Latency: Time taken for a single query execution.
     *
     * @param countryName Name of country to conduct tests on.
     * @return Results for latency experimentation.
     */
    @GetMapping("/compareLatency")
    public String compareLatencyPerformance(@RequestParam String countryName) {
        String shardedCollectionResult = listingService.measurePerformance(SHARDED, countryName);
        String nonShardedCollectionResult = listingService.measurePerformance(NONSHARDED, countryName);
        return shardedCollectionResult + "\n" + nonShardedCollectionResult;
    }

    /**
     * Get Throughput results for collections (sharded versus non-sharded).
     * Throughput: Number of queries handled per unit of time.
     *
     * @param durationInSeconds Number of seconds to query for.
     * @return Results of throughput experiment.
     */
    @GetMapping("/compareThroughput")
    public String compareThroughputPerformance(@RequestParam int durationInSeconds) {
        String shardedThroughputResult = listingService.measureThroughput(SHARDED, durationInSeconds);
        String nonShardedThroughputResult = listingService.measureThroughput(NONSHARDED, durationInSeconds);
        return shardedThroughputResult + "\n" + nonShardedThroughputResult;
    }

    /**
     * High-frequency simulation - Average latency results for 10 random countries.
     * Measure and compare the performance of two different database collection
     * setups—sharded
     * and non-sharded—under conditions that mimic a high-demand environment.
     *
     * @param collectionName Name of collection to simulate on.
     * @param location       Location to base queries on.
     * @param queryCount     Number of queries to conduct.
     * @return Response entity object containing the results of experimentation.
     */
    @GetMapping("/simulateHighFrequencyQuerying")
    public ResponseEntity<String> simulateHighFrequencyQuerying(
            @RequestParam String collectionName,
            @RequestParam String location,
            @RequestParam int queryCount) {

        String result = listingService.simulateHighFrequencyQuerying(collectionName, location, queryCount);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint used in the Final Presentation Demo.
     *
     * @param queryCount Number of queries to conduct.
     * @return Response entity object containing the results of experimentation.
     */
    @PostMapping("/simulateHighFrequencyForDemo")
    public ResponseEntity<String> simulateHighFrequencyForDemo(
            @RequestParam int queryCount) {

        Random random = new Random();
        StringBuilder results = new StringBuilder();
        List<String> euCountries = Arrays.asList(
                "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic",
                "Denmark", "Estonia", "Finland", "France", "Germany", "Greece",
                "Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg",
                "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia",
                "Slovenia", "Spain", "Sweden");

        // Create the results directory if it doesn't exist
        String resultsDir = "sharding_experiment_results";
        File directory = new File(resultsDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Define the CSV file path and clear its content if it exists
        String csvFilePath = resultsDir + "/latency_demo_results.csv";
        File csvFile = new File(csvFilePath);
        if (csvFile.exists()) {
            try (PrintWriter writer = new PrintWriter(csvFile)) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Select a random set of 10 countries
        Collections.shuffle(euCountries, random);
        List<String> selectedCountries = euCountries.subList(0, 10);

        for (String country : selectedCountries) {

            String shardedResult = listingService.simulateHighFrequencyQuerying(SHARDED, country,
                    queryCount);
            String nonShardedResult = listingService.simulateHighFrequencyQuerying(NONSHARDED, country,
                    queryCount);

            results.append("Country: ").append(country).append("\n");
            results.append("Sharded Collection: ").append(shardedResult).append("\n");
            results.append("Non-Sharded Collection: ").append(nonShardedResult).append("\n");

            try (FileWriter fileWriter = new FileWriter(csvFilePath, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {

                if (csvFile.length() == 0) {
                    printWriter.println("collectionName,country,queryCount,averageLatency");
                }
                printWriter.printf("%s,%s,%d,%.2f\n", SHARDED, country, queryCount,
                        extractLatencyFromResult(shardedResult));
                printWriter.printf("%s,%s,%d,%.2f\n", NONSHARDED, country, queryCount,
                        extractLatencyFromResult(nonShardedResult));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok("Simulation completed. Results saved to " + csvFilePath);
    }

    /**
     * Endpoint for running demo for Final Presentation.
     *
     * @param queryCount Number of queries to send.
     * @return Response entity object containing the results of experimentation.
     */
    @PostMapping("/runFullDemo")
    public ResponseEntity<String> runFullDemo(@RequestParam int queryCount) {
        simulateHighFrequencyForDemo(queryCount);
        runDemoPlotScript();
        return ResponseEntity.ok("Simulation and plotting completed. "
                + "Check sharding_experiment_results/latency_comparison_demo.png for results.");
    }

    /**
     * Helper method to run the Python script for chart generation.
     */
    public void runDemoPlotScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "plot_latency_demo.py");
            processBuilder.directory(new File("."));
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to extract latency from result string.
     *
     * @param result Result to use for latency numbers.
     * @return Double representing latency.
     */
    private double extractLatencyFromResult(String result) {
        String latencyString = result.substring(result.lastIndexOf("Average Latency: ") + 17,
                result.lastIndexOf(" ms"));
        return Double.parseDouble(latencyString.trim());
    }
}
