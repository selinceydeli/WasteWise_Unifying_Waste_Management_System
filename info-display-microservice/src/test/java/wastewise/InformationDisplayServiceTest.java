package wastewise;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.application.InformationDisplayService;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;
import wastewise.repositories.InformationDisplayRepository;

class InformationDisplayServiceTest {

    @Mock
    private InformationDisplayRepository infoDisplayRepository;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private InformationDisplayService infoDisplayService;

    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoClient.getDatabase("InformationDisplayDB")).thenReturn(mockDatabase);
    }

    @Test
    void testAddInformationDisplay() {
        InformationDisplay infoDisplay = new InformationDisplay();

        infoDisplayService.addInformationDisplay(infoDisplay);

        verify(mongoTemplate, times(1)).insert(infoDisplay, "infoDisplay");
    }

    @Test
    void testGetInfoDisplaysByLocation() {
        Location location = new Location();
        List<InformationDisplay> expectedDisplays = new ArrayList<>();
        when(infoDisplayRepository.findByLocation(location)).thenReturn(expectedDisplays);

        List<InformationDisplay> actualDisplays = infoDisplayService.getInfoDisplaysByLocation(location);

        assertEquals(expectedDisplays, actualDisplays);
        verify(infoDisplayRepository, times(1)).findByLocation(location);
    }

    @Test
    void testGetAllInformationDisplays() {
        List<InformationDisplay> expectedDisplays = new ArrayList<>();
        when(infoDisplayRepository.findAll()).thenReturn(expectedDisplays);

        List<InformationDisplay> actualDisplays = infoDisplayService.getAllInformationDisplays();

        assertEquals(expectedDisplays, actualDisplays);
        verify(infoDisplayRepository, times(1)).findAll();
    }

    @Test
    void testGetCollectionDocumentsCount() {
        String collectionName = "infoDisplay";
        long expectedCount = 10L;
        when(mongoTemplate.getCollection(collectionName)).thenReturn(mockCollection);
        when(mockCollection.countDocuments()).thenReturn(expectedCount);

        long actualCount = infoDisplayService.getCollectionDocumentsCount(collectionName);

        assertEquals(expectedCount, actualCount);
        verify(mongoTemplate, times(1)).getCollection(collectionName);
        verify(mockCollection, times(1)).countDocuments();
    }

    @Test
    void testMeasurePerformance() {
        String collectionName = "infoDisplay";
        String countryName = "USA";
        long documentCount = 50L;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 200;

        // Mock database collection and time for performance measurement
        when(mongoClient.getDatabase("InformationDisplayDB")).thenReturn(mockDatabase);
        when(mockDatabase.getCollection(collectionName)).thenReturn(mockCollection);
        when(mockCollection.countDocuments(new Document("location.country", countryName))).thenReturn(documentCount);

        // Mock timing (simulate method duration)
        long duration = endTime - startTime;
        String expectedOutput = "Collection: " + collectionName + ", Document Count: " + documentCount
                + ", Duration: " + duration + " ms";

        // Act
        String result = infoDisplayService.measurePerformance(collectionName, countryName);

        // Assert
        assertTrue(result.contains("Collection: " + collectionName));
        assertTrue(result.contains("Document Count: " + documentCount));
        assertTrue(result.contains("Duration:"));
    }
}