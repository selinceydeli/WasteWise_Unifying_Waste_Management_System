package wastewise.user.domain;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "adminCollection")
public class Admin extends User {

    @Field
    private PermissionType permissionType;

    @Field
    private List<Location> cities;

    /**
     * Create a new Admin Object.
     *
     * @param email          Email address.
     * @param pwd            Password.
     * @param cities         List of Cities under its' juristiction.
     * @param permissionType Type of Permission.
     */
    public Admin(String email, String pwd, List<Location> cities, PermissionType permissionType) {
        super(email, pwd);
        this.cities = cities;
        this.permissionType = permissionType;
    }

    public List<Location> getCities() {
        return cities;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setCities(List<Location> cities) {
        this.cities = cities;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    /**
     * Checks whether this Admin can create another Admin.
     *
     * @param otherAdmin Admin to be created.
     * @return Bool: whether creation is possible or not.
     */
    public boolean canCreateLowerLevelAdmin(Admin otherAdmin) {
        return this.permissionType.getLevel() > otherAdmin.getPermissionType().getLevel();
    }

    @Override
    public String toString() {
        return "Admin{"
                + "email='" + getEmail() + '\''
                + "permissionType=" + permissionType
                + ", cities=" + cities
                + '}';
    }

}