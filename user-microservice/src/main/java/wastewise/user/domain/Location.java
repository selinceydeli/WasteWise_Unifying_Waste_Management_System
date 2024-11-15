package wastewise.user.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Location {

    @Field("country")
    private String country;
    @Field("city")
    private String city;

    public Location() {

    }

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
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
                + '}';
    }
}
