package wastewise.domain.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wastewise.domain.model.CountryRecyclingInfo;

@Repository
public interface CountryInfoRepository extends JpaRepository<CountryRecyclingInfo, String> {

    Optional<CountryRecyclingInfo> findInfoByCountryName(String country);

}
