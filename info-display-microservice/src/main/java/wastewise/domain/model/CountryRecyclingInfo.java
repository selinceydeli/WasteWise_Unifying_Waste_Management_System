package wastewise.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class CountryRecyclingInfo {

    @Id
    @Column(name = "country", nullable = false, unique = true)
    private String countryName;
    @Column(name = "info")
    private String recyclingInfo;

    public CountryRecyclingInfo(String countryName, String recyclingInfo) {
        this.countryName = countryName;
        this.recyclingInfo = recyclingInfo;
    }
}
