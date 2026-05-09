public enum UserType {
    Father(1),
    Mother(2),
    Son(3),
    Daughter(4);

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public static UserType fromInt(int id) {
        for (UserType type : values()) {
            if (type.value == id) {
                return type;
            }
        }
        return Son;
    }

    public int getValue() {
        return value;
    }
}