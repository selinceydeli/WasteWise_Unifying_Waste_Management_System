package wastewise.form.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wastewise.form.domain.FormItem;
import wastewise.form.domain.Location;

@Repository
public interface FormRepository extends MongoRepository<FormItem, Integer> {

    List<FormItem> findByLocation(Location location);
}