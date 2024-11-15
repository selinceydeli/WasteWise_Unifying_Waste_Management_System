package wastewise.listing.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    @Primary
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27117");
    }

    @Bean
    MongoOperations mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "ListingDB");
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactor(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "ListingDB");
    }
}