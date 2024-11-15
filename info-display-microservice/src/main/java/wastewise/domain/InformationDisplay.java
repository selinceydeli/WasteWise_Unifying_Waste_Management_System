package wastewise.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@Getter
@Setter
@Document(collection = "infoDisplay")
public class InformationDisplay {

    @Id
    private String id;

    @Field("location")
    Location location;

    @Field("general_info")
    private String generalInfo;

    @Field("bin_locations")
    private String binLocations;

    @Field("pickup_schedule")
    private String pickupSchedule;

    public InformationDisplay() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(String generalInfo) {
        this.generalInfo = generalInfo;
    }

    public String getBinLocations() {
        return binLocations;
    }

    public void setBinLocations(String binLocations) {
        this.binLocations = binLocations;
    }

    public String getPickupSchedule() {
        return pickupSchedule;
    }

    public void setPickupSchedule(String pickupSchedule) {
        this.pickupSchedule = pickupSchedule;
    }

    @Override
    public String toString() {
        return "InformationDisplay{"
                + "id='" + id + '\''
                + ", location=" + location
                + ", general_info='" + generalInfo + '\''
                + ", bin_locations='" + binLocations + '\''
                + ", pickup_schedule='" + pickupSchedule + '\''
                + '}';
    }
}
