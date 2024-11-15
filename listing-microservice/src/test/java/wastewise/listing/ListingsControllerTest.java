package wastewise.listing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wastewise.listing.application.ListingService;
import wastewise.listing.controllers.ListingsController;
import wastewise.listing.database.PopulateDatabase;
import wastewise.listing.domain.Listing;
import wastewise.listing.domain.MaterialType;

public class ListingsControllerTest {

    @Mock
    private ListingService listingService;

    @Mock
    private PopulateDatabase populateDatabase;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ListingsController listingsController;

    private MockMvc mockMvc;
    private static final String SHARDED = "listingCollection";
    private static final String NONSHARDED = "nonShardedCollection";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listingsController).build();
    }

    @Test
    public void testViewAllListings() throws Exception {
        Listing listing1 = new Listing();
        listing1.setId("1");
        listing1.setCompanyName("Listing1");

        Listing listing2 = new Listing();
        listing2.setId("2");
        listing2.setCompanyName("Listing2");

        List<Listing> listings = Arrays.asList(listing1, listing2);
        when(listingService.getAllListings()).thenReturn(listings);

        mockMvc.perform(get("/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Listing1"))
                .andExpect(jsonPath("$[1].companyName").value("Listing2"));
    }

    @Test
    public void testAddListing() throws Exception {
        Listing listing = new Listing();
        listing.setCompanyName("NewListing");
        doNothing().when(listingService).addListing(any(Listing.class));

        mockMvc.perform(post("/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"company_name\":\"NewListing\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Listing added successfully."));
    }

    @Test
    public void testGetListingsByLocation() throws Exception {

        Listing listing = new Listing();
        listing.setLocation("Delft");
        listing.setCompanyName("CompanyListing");
        List<Listing> listings = Collections.singletonList(listing);
        when(listingService.getListingsByLocation("Delft")).thenReturn(listings);

        mockMvc.perform(get("/listings/by-location")
                .param("location", "Delft"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("CompanyListing"));
    }

    @Test
    public void testGetListingsByCompanyName() throws Exception {

        Listing listing = new Listing();
        listing.setCompanyName("CompanyListing");
        List<Listing> listings = Collections.singletonList(listing);
        when(listingService.getListingsByCompanyName("TestCompany")).thenReturn(listings);

        mockMvc.perform(get("/listings/by-company")
                .param("companyName", "TestCompany"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("CompanyListing"));
    }

    @Test
    public void testGetListingsByCompanyName_NoContent() throws Exception {
        when(listingService.getListingsByCompanyName("NonExistentCompany")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listings/by-company")
                .param("companyName", "NonExistentCompany"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetListingsByMaterial() throws Exception {
        Listing listing = new Listing();
        listing.setMaterial(MaterialType.PLASTIC);
        listing.setCompanyName("MaterialListing");
        List<Listing> listings = Collections.singletonList(listing);
        when(listingService.getListingsByMaterial("Plastic")).thenReturn(listings);

        mockMvc.perform(get("/listings/by-material")
                .param("material", "Plastic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("MaterialListing"));
    }

    @Test
    public void testGetListingsMaterial_NoContent() throws Exception {
        when(listingService.getListingsByMaterial("NonExistentMaterial")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listings/by-material")
                .param("material", "NonExistentMaterial"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCountOfListings() throws Exception {
        when(listingService.getCountOfListings()).thenReturn(10L);

        mockMvc.perform(post("/listings/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    public void testCreateCollection_CreatesNewCollection() throws Exception {
        String collectionName = "newCollection";
        when(mongoTemplate.collectionExists(collectionName)).thenReturn(false);
        doNothing().when(populateDatabase).createCollectionIfNotExists(collectionName);

        mockMvc.perform(post("/listings/createCollection")
                .param("collectionName", collectionName))
                .andExpect(status().isOk())
                .andExpect(content().string("Collection created successfully: " + collectionName));
    }

    @Test
    public void testCreateCollection_CollectionAlreadyExists() throws Exception {
        String collectionName = "existingCollection";
        when(mongoTemplate.collectionExists(collectionName)).thenReturn(true);

        mockMvc.perform(post("/listings/createCollection")
                .param("collectionName", collectionName))
                .andExpect(status().isOk())
                .andExpect(content().string("Collection already exists: " + collectionName));
    }

    @Test
    public void testCompareLatencyPerformance() throws Exception {
        String countryName = "TestCountry";
        String shardedResult = "Sharded Latency: 120ms";
        String nonShardedResult = "Non-Sharded Latency: 150ms";

        when(listingService.measurePerformance(SHARDED, countryName)).thenReturn(shardedResult);
        when(listingService.measurePerformance(NONSHARDED, countryName)).thenReturn(nonShardedResult);

        mockMvc.perform(get("/listings/compareLatency")
                .param("countryName", countryName))
                .andExpect(status().isOk())
                .andExpect(content().string(shardedResult + "\n" + nonShardedResult));
    }

    @Test
    public void testCompareThroughputPerformance() throws Exception {
        int durationInSeconds = 10;
        String shardedThroughputResult = "Sharded Throughput: 200 requests/sec";
        String nonShardedThroughputResult = "Non-Sharded Throughput: 180 requests/sec";

        when(listingService.measureThroughput(SHARDED, durationInSeconds)).thenReturn(shardedThroughputResult);
        when(listingService.measureThroughput(NONSHARDED, durationInSeconds)).thenReturn(nonShardedThroughputResult);

        mockMvc.perform(get("/listings/compareThroughput")
                .param("durationInSeconds", String.valueOf(durationInSeconds)))
                .andExpect(status().isOk())
                .andExpect(content().string(shardedThroughputResult + "\n" + nonShardedThroughputResult));
    }

    @Test
    public void testPopulateCollections() throws Exception {
        when(listingService.getCollectionDocumentsCount(anyString())).thenReturn(0L, 5L);
        doNothing().when(populateDatabase).insertListingsIntoCollection(anyList(), anyString());

        mockMvc.perform(post("/listings/populate")
                .param("collectionNames", "collection1,collection2")
                .param("n", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Some collections failed!"));
    }
}