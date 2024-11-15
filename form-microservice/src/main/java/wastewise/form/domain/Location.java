package wastewise.form.domain;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Location object that allows to separate Forms per regions.
 */
@NoArgsConstructor
public class Location {

    @Field("country")
    private String country;
    @Field("city")
    private String city;
    @Field("postcode")
    private String postcode;

    /**
     * Instantiates a location.
     *
     * @param country  Country of location.
     * @param city     City of location.
     * @param postcode Postcode of location.
     */
    public Location(String country, String city, String postcode) {
        this.country = country;
        this.city = city;
        this.postcode = postcode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "{"
                + "country='" + country + '\''
                + ", city='" + city + '\''
                + ", postcode='" + postcode + '\''
                + '}';
    }
}