package wastewise.listing.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import wastewise.listing.domain.Listing;

@Repository
public interface ListingRepository extends MongoRepository<Listing, Integer> {

    @Query("{ 'company_name': { $regex: ?0, $options: 'i' } }")
    List<Listing> findByCompanyName(String companyName);

    List<Listing> findByMaterial(String material);

    List<Listing> findByLocation(String location);
}
