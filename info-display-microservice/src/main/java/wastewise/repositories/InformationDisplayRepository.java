package wastewise.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wastewise.domain.InformationDisplay;
import wastewise.domain.Location;

@Repository
public interface InformationDisplayRepository extends MongoRepository<InformationDisplay, Integer> {

    List<InformationDisplay> findByLocation(Location location);
}
