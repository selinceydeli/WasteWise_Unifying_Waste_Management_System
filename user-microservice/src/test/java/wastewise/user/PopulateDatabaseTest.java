package wastewise.user;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import wastewise.user.database.PopulateDatabase;

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
    void testGenerateAndInsertRandomUsers_InsertsAdmins() {
        int n = 5;
        String collectionName = "adminCollection";

        populateDatabase.generateAndInsertRandomUsers(n, collectionName);

        verify(mongoTemplate).insert(anyList(), eq(collectionName));
    }

    @Test
    void testGenerateAndInsertRandomUsers_InsertsCompanies() {
        int n = 3;
        String collectionName = "companyCollection";

        populateDatabase.generateAndInsertRandomUsers(n, collectionName);

        verify(mongoTemplate).insert(anyList(), eq(collectionName));
    }

    @Test
    void testGenerateAndInsertRandomUsers_GeneratesCorrectNumberOfAdmins() {
        int n = 10;
        String collectionName = "adminCollection";

        populateDatabase.generateAndInsertRandomUsers(n, collectionName);
        // We can't directly assert the size of the inserted list here because we don't expose the method that generates it,
        // but we can verify that the insert method is called with a non-empty list
        verify(mongoTemplate).insert(argThat(list -> list.size() == n), eq(collectionName));
    }

    @Test
    void testGenerateAndInsertRandomUsers_GeneratesCorrectNumberOfCompanies() {
        int n = 7;
        String collectionName = "companyCollection";

        populateDatabase.generateAndInsertRandomUsers(n, collectionName);
        // Similar as before, ensuring the inserted list has the correct number of companies
        verify(mongoTemplate).insert(argThat(list -> list.size() == n), eq(collectionName));
    }
}