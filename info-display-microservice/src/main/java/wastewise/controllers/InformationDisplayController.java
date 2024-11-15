package wastewise.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wastewise.application.InformationDisplayService;
import wastewise.database.PopulateDatabase;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;

/**
 * Information Display Controller.
 */
@RestController
@RequestMapping("/infodisplay")
public class InformationDisplayController {

    private final transient InformationDisplayService infoDisplayService;
    private final transient PopulateDatabase populateDatabase;

    @Autowired
    public InformationDisplayController(InformationDisplayService infoDisplayService,
            PopulateDatabase populateDatabase) {
        this.infoDisplayService = infoDisplayService;
        this.populateDatabase = populateDatabase;
    }

    @GetMapping
    public ResponseEntity<List<InformationDisplay>> viewAllInformationDisplays() {
        List<InformationDisplay> infoDisplays = infoDisplayService.getAllInformationDisplays();
        return ResponseEntity.ok(infoDisplays);
    }

    /**
     * Returns a list of Information Displays corresponding to the given location.
     *
     * @param location Location to fetch information displays for.
     * @return List of Information Displays.
     */
    @GetMapping("/by-location")
    public ResponseEntity<List<InformationDisplay>> getInfoDisplayByLocation(@RequestBody Location location) {
        List<InformationDisplay> infoDisplays = infoDisplayService.getInfoDisplaysByLocation(location);
        if (infoDisplays.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(infoDisplays);
    }

    /**
     * Populates a collection for testing purposes.
     *
     * @param n Population count.
     * @return Response message.
     */
    @PostMapping("/populate")
    public ResponseEntity<String> populateCollection(@RequestParam int n) {
        long documentCountBefore = infoDisplayService.getCollectionDocumentsCount("infoDisplay");
        System.out.println("countBefore = " + documentCountBefore);
        populateDatabase.generateAndInsertRandomDocuments(n);
        long documentCountAfter = infoDisplayService.getCollectionDocumentsCount("infoDisplay");
        System.out.println("countAfter = " + documentCountAfter);
        if (documentCountAfter - documentCountBefore == n) {
            return ResponseEntity.ok(n + " documents inserted successfully!");
        } else {
            return ResponseEntity.ok("uh oh!!!");
        }
    }

    /**
     * Compares performance of sharded and non-sharded databases.
     *
     * @param countryName Name of country to evaluate performance on.
     * @return Results as string.
     */
    @GetMapping("/compare")
    public String comparePerformance(@RequestParam String countryName) {
        String shardedCollectionResult = infoDisplayService.measurePerformance("infoDisplay", countryName);
        String nonShardedCollectionResult = infoDisplayService.measurePerformance("nonShardedInfoDisplay", countryName);

        return shardedCollectionResult + "\n" + nonShardedCollectionResult;
    }
}
