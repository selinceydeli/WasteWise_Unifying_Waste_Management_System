package wastewise.user.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import wastewise.user.domain.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, Integer> {

    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    Admin findByEmail(String email);

    List<Admin> findAll();
}
