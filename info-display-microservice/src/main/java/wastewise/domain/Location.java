package wastewise.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Location {

    @Field("country")
    private String country;
    @Field("city")
    private String city;
    @Field("postcode")
    private String postcode;

    public Location() {

    }

    /**
     * Creates new Location object.
     *
     * @param country  Country.
     * @param city     City.
     * @param postcode Postcode.
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
        return "{" + "country='"
                + country + '\''
                + ", city='" + city + '\''
                + ", postcode='" + postcode + '\''
                + '}';
    }
}
