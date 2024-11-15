package wastewise.user.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "companyCollection")
public class Company extends User {

    @Field
    private String name;

    @Field
    private Date lastVerified; // When the company was last verified

    @Field
    private String description;

    @Field
    private List<MaterialType> listMaterial; // List of materials (assuming this is an enum)

    @Field
    private Location location;

    /**
     * Creates a new Company Object.
     *
     * @param email        Email address.
     * @param pwd          Password.
     * @param name         Name of the Company.
     * @param lastVerified Last date of verification by authorities.
     * @param description  Description of Company.
     * @param listMaterial List of materials Company can process.
     * @param location     Location of the Company.
     */
    public Company(String email, String pwd, String name, Date lastVerified, String description,
            List<MaterialType> listMaterial, Location location) {
        super(email, pwd); // Inherit email and password from User
        this.name = name;
        this.lastVerified = lastVerified;
        this.description = description;
        this.listMaterial = listMaterial;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastVerified() {
        return lastVerified;
    }

    public void setLastVerified(Date lastVerified) {
        this.lastVerified = lastVerified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MaterialType> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(List<MaterialType> listMaterial) {
        this.listMaterial = listMaterial;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Company{"
                + "name='" + name + '\''
                + ", lastVerified=" + lastVerified
                + ", description='" + description + '\''
                + ", listMaterial=" + listMaterial
                + ", location='" + location.toString() + '\''
                + ", email='" + getEmail() + '\''
                + '}';
    }
}
