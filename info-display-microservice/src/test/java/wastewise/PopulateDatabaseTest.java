package wastewise;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.database.PopulateDatabase;
import wastewise.domain.InformationDisplay;

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
    void testGenerateAndInsertRandomDocuments() {
        int n = 5;

        // Act
        populateDatabase.generateAndInsertRandomDocuments(n);

        // Assert
        verify(mongoTemplate, times(1)).insertAll(any(List.class));
    }
}