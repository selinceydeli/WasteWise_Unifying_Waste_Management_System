package wastewise.domain;

import java.util.Optional;
import org.springframework.stereotype.Service;
import wastewise.domain.model.CountryRecyclingInfo;
import wastewise.domain.repo.CountryInfoRepository;

@Service
public class RecyclingInformationService {

    private final transient CountryInfoRepository countryInfoRepository;

    public RecyclingInformationService(CountryInfoRepository countryInfoRepository) {
        this.countryInfoRepository = countryInfoRepository;
    }

    /**
     * Adds countries to the Database.
     */
    public void addCountriesToDB() {
        CountryRecyclingInfo poland = new CountryRecyclingInfo("PL",
                "Recycling in Poland is great! You do it like this!");
        CountryRecyclingInfo netherlands = new CountryRecyclingInfo("NL",
                "Recycling in Netherlands is great! You do it like this!");
        CountryRecyclingInfo turkey = new CountryRecyclingInfo("TR",
                "Recycling in Turkey is great! You do it like this!");
        CountryRecyclingInfo romania = new CountryRecyclingInfo("RO",
                "Recycling in Romania is great! You do it like this!");
        countryInfoRepository.save(poland);
        countryInfoRepository.save(netherlands);
        countryInfoRepository.save(romania);
        countryInfoRepository.save(turkey);
    }

    /**
     * Fetches description of given country.
     *
     * @param country Country to search for in the database.
     * @return Description (if exists).
     */
    public String getCountryDescription(String country) {
        Optional<CountryRecyclingInfo> info = countryInfoRepository.findInfoByCountryName(country);
        if (info.isEmpty()) {
            return "Sorry, our website does not operate for your country yet!";
        }
        return info.get().getRecyclingInfo();
    }
}
