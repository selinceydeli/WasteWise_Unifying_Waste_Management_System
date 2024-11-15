package wastewise.user.domain;

import java.util.List;

public class AdminCreationRequest {
    private String upperAdminEmail;
    private String email;
    private List<Location> locations;
    private PermissionType permissionType;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public String getUpperAdminEmail() {
        return upperAdminEmail;
    }

    public void setUpperAdminEmail(String upperAdminEmail) {
        this.upperAdminEmail = upperAdminEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
