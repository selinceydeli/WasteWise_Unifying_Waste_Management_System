package wastewise.form.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wastewise.form.application.FormService;
import wastewise.form.database.PopulateDatabase;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;

/**
 * Information Display Controller.
 *
 */
@RestController
@RequestMapping("/form")
public class FormController {

    private final transient FormService formService;
    private final transient PopulateDatabase populateDatabase;

    @Autowired
    public FormController(FormService formService, PopulateDatabase populateDatabase) {
        this.formService = formService;
        this.populateDatabase = populateDatabase;
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<FormItem>> viewAllForms() {
        List<FormItem> infoDisplays = formService.getAllForms();
        return ResponseEntity.ok(infoDisplays);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello User!");
    }

    @PostMapping("/add")
    public ResponseEntity<FormItem> addForm(@RequestBody FormItem formItem) {
        formService.addForm(formItem);
        return ResponseEntity.ok(formItem);
    }

    /**
     * Returns a list of Form Items corresponding to the given location.
     *
     * @param location Location to fetch forms for.
     * @return List of Forms.
     */
    @GetMapping("/by-location")
    public ResponseEntity<List<FormItem>> getFormByLocation(@RequestBody Location location) {
        List<FormItem> forms = formService.getFormByLocation(location);
        if (forms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(forms);
    }

    /**
     * Populates a collection for testing purposes.
     *
     * @param n Population count.
     * @return Response message.
     */
    @PostMapping("/populate")
    public ResponseEntity<String> populateCollection(@RequestParam int n) {
        long documentCountBefore = formService.getCollectionDocumentsCount("formCollection");
        System.out.println("countBefore = " + documentCountBefore);
        populateDatabase.generateAndInsertRandomForms(n);
        long documentCountAfter = formService.getCollectionDocumentsCount("formCollection");
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
        String shardedCollectionResult = formService.measurePerformance("formCollection", countryName);
        String nonShardedCollectionResult = formService.measurePerformance("nonShardedInfoDisplay", countryName);

        return shardedCollectionResult + "\n" + nonShardedCollectionResult;
    }
}