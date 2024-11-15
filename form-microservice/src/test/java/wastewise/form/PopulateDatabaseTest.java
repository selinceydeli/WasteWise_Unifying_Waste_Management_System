package wastewise.form;

import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.form.database.PopulateDatabase;
import wastewise.form.domain.FormItem;

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
    void testGenerateAndInsertRandomForms_InsertsForms() {
        int n = 5;

        populateDatabase.generateAndInsertRandomForms(n);

        // Verify that insertAll was called with a list of the expected size
        verify(mongoTemplate).insertAll(argThat(forms -> forms.size() == n));
    }

    @Test
    void testGenerateAndInsertRandomForms_GeneratesCorrectNumberOfForms() {
        int n = 3;

        populateDatabase.generateAndInsertRandomForms(n);

        // Ensure the generated forms are inserted into the collection
        verify(mongoTemplate).insertAll(argThat(forms -> forms.size() == n));
    }

    @Test
    void testGenerateAndInsertRandomForms_GeneratesFormsWithLocations() {
        int n = 1; // Test with 1 form

        populateDatabase.generateAndInsertRandomForms(n);

        // Verify that insertAll was called and check the generated form's location
        verify(mongoTemplate).insertAll(argThat(forms -> {
            // Cast to List<FormItem> for type resolution
            List<FormItem> formList = (List<FormItem>) forms;
            FormItem form = formList.get(0);
            return form.getLocation() != null
                    && form.getLocation().getCountry() != null
                    && form.getLocation().getCity() != null
                    && form.getLocation().getPostcode() != null;
        }));
    }

    // Additional tests can be added to cover more scenarios or edge cases
}