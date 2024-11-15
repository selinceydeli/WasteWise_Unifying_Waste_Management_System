package wastewise.listing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.listing.database.PopulateDatabase;
import wastewise.listing.domain.Listing;

class PopulateDatabaseTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PopulateDatabase populateDatabase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCollectionIfNotExists_CreatesNewCollection() {
        String collectionName = "newCollection";

        when(mongoTemplate.collectionExists(collectionName)).thenReturn(false);

        populateDatabase.createCollectionIfNotExists(collectionName);

        verify(mongoTemplate).createCollection(collectionName);
        verify(mongoTemplate).collectionExists(collectionName);
    }

    @Test
    void testCreateCollectionIfNotExists_CollectionAlreadyExists() {
        String collectionName = "existingCollection";

        when(mongoTemplate.collectionExists(collectionName)).thenReturn(true);

        populateDatabase.createCollectionIfNotExists(collectionName);

        verify(mongoTemplate, never()).createCollection(collectionName);
        verify(mongoTemplate).collectionExists(collectionName);
    }

    @Test
    void testInsertListingsIntoCollection() {
        String collectionName = "listingsCollection";
        List<Listing> listings = List.of(new Listing(), new Listing());

        populateDatabase.insertListingsIntoCollection(listings, collectionName);

        verify(mongoTemplate).insert(listings, collectionName);
    }

    @Test
    void testGenerateRandomListings() {
        int n = 5;

        List<Listing> listings = populateDatabase.generateRandomListings(n);

        assertEquals(n, listings.size());
    }
}