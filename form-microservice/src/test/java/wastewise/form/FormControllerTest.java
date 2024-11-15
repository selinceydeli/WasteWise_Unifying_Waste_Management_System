package wastewise.form;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import wastewise.form.application.FormService;
import wastewise.form.controllers.FormController;
import wastewise.form.database.PopulateDatabase;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;

class FormControllerTest {

    @Mock
    private FormService formService;

    @Mock
    private PopulateDatabase populateDatabase;

    @InjectMocks
    private FormController formController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewAllForms() {
        FormItem formItem = new FormItem();
        List<FormItem> expectedForms = List.of(formItem);

        when(formService.getAllForms()).thenReturn(expectedForms);

        ResponseEntity<List<FormItem>> response = formController.viewAllForms();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedForms, response.getBody());
        verify(formService).getAllForms();
    }

    @Test
    void testHelloWorld() {
        ResponseEntity<String> response = formController.helloWorld();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hello User!", response.getBody());
    }

    @Test
    void testAddForm() {
        FormItem formItem = new FormItem();
        formItem.setName("Test Form");

        ResponseEntity<FormItem> response = formController.addForm(formItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(formItem, response.getBody());
        verify(formService).addForm(formItem);
    }

    @Test
    void testGetFormByLocation_WithForms() {
        Location location = new Location();
        location.setCountry("UK");
        FormItem formItem = new FormItem();
        formItem.setLocation(location);
        List<FormItem> expectedForms = List.of(formItem);

        when(formService.getFormByLocation(location)).thenReturn(expectedForms);

        ResponseEntity<List<FormItem>> response = formController.getFormByLocation(location);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedForms, response.getBody());
        verify(formService).getFormByLocation(location);
    }

    @Test
    void testGetFormByLocation_NoContent() {
        Location location = new Location();
        location.setCountry("UK");

        when(formService.getFormByLocation(location)).thenReturn(Collections.emptyList());

        ResponseEntity<List<FormItem>> response = formController.getFormByLocation(location);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(formService).getFormByLocation(location);
    }

    @Test
    void testPopulateCollection_Success() {
        int n = 5;
        long countBefore = 0;
        long countAfter = n;

        when(formService.getCollectionDocumentsCount("formCollection")).thenReturn(countBefore, countAfter);

        ResponseEntity<String> response = formController.populateCollection(n);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(n + " documents inserted successfully!", response.getBody());
        verify(populateDatabase).generateAndInsertRandomForms(n);
        verify(formService, times(2)).getCollectionDocumentsCount("formCollection");
    }

    @Test
    void testPopulateCollection_Failure() {
        int n = 5;
        long countBefore = 0;
        long countAfter = 3; // Simulate a failure

        when(formService.getCollectionDocumentsCount("formCollection")).thenReturn(countBefore, countAfter);

        ResponseEntity<String> response = formController.populateCollection(n);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("uh oh!!!", response.getBody());
        verify(populateDatabase).generateAndInsertRandomForms(n);
        verify(formService, times(2)).getCollectionDocumentsCount("formCollection");
    }

    @Test
    void testComparePerformance() {
        String countryName = "UK";
        String shardedResult = "Sharded: 100 documents";
        String nonShardedResult = "Non-Sharded: 200 documents";

        when(formService.measurePerformance("formCollection", countryName)).thenReturn(shardedResult);
        when(formService.measurePerformance("nonShardedInfoDisplay", countryName)).thenReturn(nonShardedResult);

        String response = formController.comparePerformance(countryName);

        assertTrue(response.contains(shardedResult));
        assertTrue(response.contains(nonShardedResult));
        verify(formService).measurePerformance("formCollection", countryName);
        verify(formService).measurePerformance("nonShardedInfoDisplay", countryName);
    }
}