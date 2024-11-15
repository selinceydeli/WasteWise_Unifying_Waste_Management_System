package wastewise.user.domain;

import java.util.List;

public class CompanyCreationRequest {
    private String adminEmail;
    private String email;
    private String name;
    private String description;
    private List<MaterialType> listMaterial;
    private Location location;

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
