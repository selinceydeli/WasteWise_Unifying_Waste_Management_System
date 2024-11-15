package wastewise.user.domain;

public enum PermissionType {
    PERMISSION_1(1),
    PERMISSION_2(2),
    PERMISSION_3(3);

    private final int level;

    PermissionType(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}