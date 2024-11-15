package wastewise.form;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.form.application.FormService;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;
import wastewise.form.repository.FormRepository;

class FormServiceTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private FormService formService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoClient.getDatabase("FormDB")).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(mongoCollection);
    }

    @Test
    void testAddForm() {
        FormItem formItem = new FormItem();
        formItem.setName("Test Form");

        formService.addForm(formItem);

        verify(mongoTemplate).insert(formItem, "formCollection");
    }

    @Test
    void testGetFormByLocation() {
        Location location = new Location();
        location.setCountry("UK");
        FormItem formItem = new FormItem();
        formItem.setLocation(location);
        List<FormItem> expectedForms = List.of(formItem);

        when(formRepository.findByLocation(location)).thenReturn(expectedForms);

        List<FormItem> result = formService.getFormByLocation(location);

        assertEquals(expectedForms, result);
        verify(formRepository).findByLocation(location);
    }

    @Test
    void testGetAllForms() {
        FormItem formItem1 = new FormItem();
        FormItem formItem2 = new FormItem();
        List<FormItem> expectedForms = List.of(formItem1, formItem2);

        when(formRepository.findAll()).thenReturn(expectedForms);

        List<FormItem> result = formService.getAllForms();

        assertEquals(expectedForms, result);
        verify(formRepository).findAll();
    }

    @Test
    void testGetCollectionDocumentsCount() {
        String collectionName = "formCollection";
        long expectedCount = 10L;

        when(mongoTemplate.getCollection(collectionName)).thenReturn(mongoCollection);
        when(mongoCollection.countDocuments()).thenReturn(expectedCount);

        long result = formService.getCollectionDocumentsCount(collectionName);

        assertEquals(expectedCount, result);
        verify(mongoTemplate).getCollection(collectionName);
        verify(mongoCollection).countDocuments();
    }

    @Test
    void testMeasurePerformance() {
        String collectionName = "formCollection";
        String countryName = "UK";
        long documentCount = 5L;

        when(mongoCollection.countDocuments(new Document("location.country", countryName))).thenReturn(documentCount);

        String result = formService.measurePerformance(collectionName, countryName);

        assertTrue(result.contains("Collection: " + collectionName));
        assertTrue(result.contains("Document Count: " + documentCount));
        assertTrue(result.contains("Duration: "));
    }
}