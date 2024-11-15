package wastewise;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wastewise.application.InformationDisplayService;
import wastewise.controllers.InformationDisplayController;
import wastewise.database.PopulateDatabase;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;

class InformationDisplayControllerTest {

    @Mock
    private InformationDisplayService infoDisplayService;

    @Mock
    private PopulateDatabase populateDatabase;

    @InjectMocks
    private InformationDisplayController informationDisplayController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(informationDisplayController).build();
    }

    @Test
    void testViewAllInformationDisplays() throws Exception {
        List<InformationDisplay> infoDisplays = new ArrayList<>();
        when(infoDisplayService.getAllInformationDisplays()).thenReturn(infoDisplays);

        mockMvc.perform(get("/infodisplay")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(infoDisplays)));

        verify(infoDisplayService, times(1)).getAllInformationDisplays();
    }

    @Test
    void testGetInfoDisplayByLocation_NoContent() throws Exception {
        Location location = new Location();
        location.setCountry("USA");
        location.setCity("New York");

        when(infoDisplayService.getInfoDisplaysByLocation(refEq(location))).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/infodisplay/by-location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isNoContent());

        verify(infoDisplayService, times(1)).getInfoDisplaysByLocation(refEq(location));
    }

    @Test
    void testPopulateCollection() throws Exception {
        int n = 5;
        long documentCountBefore = 10;
        long documentCountAfter = 15;

        when(infoDisplayService.getCollectionDocumentsCount("infoDisplay")).thenReturn(documentCountBefore,
                documentCountAfter);

        mockMvc.perform(post("/infodisplay/populate")
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(n + " documents inserted successfully!"));

        verify(infoDisplayService, times(2)).getCollectionDocumentsCount("infoDisplay");
        verify(populateDatabase, times(1)).generateAndInsertRandomDocuments(n);
    }

    @Test
    void testComparePerformance() throws Exception {
        String countryName = "USA";
        String shardedResult = "Collection: infoDisplay, Document Count: 100, Duration: 50 ms";
        String nonShardedResult = "Collection: nonShardedInfoDisplay, Document Count: 120, Duration: 60 ms";

        when(infoDisplayService.measurePerformance("infoDisplay", countryName)).thenReturn(shardedResult);
        when(infoDisplayService.measurePerformance("nonShardedInfoDisplay", countryName)).thenReturn(nonShardedResult);

        mockMvc.perform(get("/infodisplay/compare")
                        .param("countryName", countryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(shardedResult + "\n" + nonShardedResult));

        verify(infoDisplayService, times(1)).measurePerformance("infoDisplay", countryName);
        verify(infoDisplayService, times(1)).measurePerformance("nonShardedInfoDisplay", countryName);
    }
}