package wastewise.user.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import wastewise.user.domain.Company;

@Repository
public interface CompanyRepository extends MongoRepository<Company, Integer> {

    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    Company findByEmail(String email);

    List<Company> findAll();
}
