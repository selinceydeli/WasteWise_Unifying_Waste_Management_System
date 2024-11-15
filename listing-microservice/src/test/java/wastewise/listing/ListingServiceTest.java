package wastewise.listing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.listing.application.ListingService;
import wastewise.listing.domain.Listing;
import wastewise.listing.domain.MaterialType;
import wastewise.listing.repositories.ListingRepository;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoClient mongoClient;

    @InjectMocks
    private ListingService listingService;

    @BeforeEach
    public void setUp() {
        listingService = new ListingService(listingRepository, mongoTemplate, mongoClient);
    }

    @Test
    public void testGetCountOfListings() {
        when(listingRepository.count()).thenReturn(5L);

        long count = listingService.getCountOfListings();

        assertEquals(5L, count);
        verify(listingRepository).count();
    }

    @Test
    public void testGetAllListings() {
        Listing listing1 = new Listing();
        listing1.setCompanyName("Company A");
        listing1.setMaterial(MaterialType.EWASTE);
        listing1.setLocation("Location A");
        Listing listing2 = new Listing();
        listing1.setCompanyName("Company B");
        listing1.setMaterial(MaterialType.ORGANIC);
        listing1.setLocation("Location B");
        List<Listing> listings = Arrays.asList(listing1, listing2);

        when(listingRepository.findAll()).thenReturn(listings);

        List<Listing> result = listingService.getAllListings();

        assertEquals(2, result.size());
        verify(listingRepository).findAll();
    }

    @Test
    public void testAddListing() {
        Listing listing = new Listing();
        listing.setCompanyName("Company A");
        listing.setMaterial(MaterialType.METAL);
        listing.setLocation("Location A");
        listingService.addListing(listing);

        verify(listingRepository).save(listing);
    }

    @Test
    public void testGetListingsByCompanyName() {
        String companyName = "Company A";
        Listing listing = new Listing();
        listing.setCompanyName(companyName);
        listing.setMaterial(MaterialType.METAL);
        listing.setLocation("Location A");
        List<Listing> listings = Collections.singletonList(listing);

        when(listingRepository.findByCompanyName(companyName)).thenReturn(listings);

        List<Listing> result = listingService.getListingsByCompanyName(companyName);

        assertEquals(1, result.size());
        assertEquals(companyName, result.get(0).getCompanyName());
        verify(listingRepository).findByCompanyName(companyName);
    }

    @Test
    public void testGetListingsByMaterial() {
        Listing listing = new Listing();
        listing.setCompanyName("Company A");
        MaterialType material = MaterialType.METAL;
        listing.setMaterial(MaterialType.METAL);
        listing.setLocation("Location A");
        List<Listing> listings = Collections.singletonList(listing);

        when(listingRepository.findByMaterial(String.valueOf(material))).thenReturn(listings);

        List<Listing> result = listingService.getListingsByMaterial(String.valueOf(material));

        assertEquals(1, result.size());
        assertEquals(material, result.get(0).getMaterial());
        verify(listingRepository).findByMaterial(String.valueOf(material));
    }

    @Test
    public void testGetListingsByLocation() {
        String location = "Location A";
        Listing listing = new Listing();
        listing.setCompanyName("Company A");
        listing.setMaterial(MaterialType.PAPER);
        listing.setLocation(location);
        List<Listing> listings = Collections.singletonList(listing);

        when(listingRepository.findByLocation(location)).thenReturn(listings);

        List<Listing> result = listingService.getListingsByLocation(location);

        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(listingRepository).findByLocation(location);
    }

    @Test
    public void testGetCollectionDocumentsCount() {
        String collectionName = "someCollection";
        MongoDatabase database = mock(MongoDatabase.class);
        MongoCollection<Document> collection = mock(MongoCollection.class);

        // Mock the mongoTemplate to return the mocked collection when getCollection is called
        when(mongoTemplate.getCollection(collectionName)).thenReturn(collection);
        when(collection.countDocuments()).thenReturn(10L);

        long count = listingService.getCollectionDocumentsCount(collectionName);

        assertEquals(10L, count);
        verify(collection).countDocuments();
    }

    @Test
    public void testMeasurePerformance() {
        String collectionName = "someCollection";
        String countryName = "Country A";
        MongoDatabase database = mock(MongoDatabase.class);
        MongoCollection<Document> collection = mock(MongoCollection.class);
        when(mongoClient.getDatabase("ListingDB")).thenReturn(database);
        when(database.getCollection(collectionName)).thenReturn(collection);
        when(collection.countDocuments(any(Document.class))).thenReturn(5L);

        String result = listingService.measurePerformance(collectionName, countryName);

        assertEquals("Collection: someCollection, Document Count: 5, Duration: ",
                result.substring(0, result.indexOf(" ms") - 1));
        verify(collection).countDocuments(new Document("location", countryName));
    }

    @Test
    public void testMeasureThroughput() {
        String collectionName = "someCollection";
        int durationInSeconds = 1;

        String result = listingService.measureThroughput(collectionName, durationInSeconds);

        assertTrue(result.startsWith("Throughput for someCollection:"));
    }

    @Test
    public void testSimulateHighFrequencyQuerying() {
        String collectionName = "someCollection";
        String location = "Location A";
        int queryCount = 5;
        MongoDatabase database = mock(MongoDatabase.class);
        MongoCollection<Document> collection = mock(MongoCollection.class);
        when(mongoClient.getDatabase("ListingDB")).thenReturn(database);
        when(database.getCollection(collectionName)).thenReturn(collection);
        when(collection.countDocuments(any(Document.class))).thenReturn(1L);

        String result = listingService.simulateHighFrequencyQuerying(collectionName, location, queryCount);

        assertTrue(result.startsWith("Collection: someCollection, Location: Location A,"));
    }
}