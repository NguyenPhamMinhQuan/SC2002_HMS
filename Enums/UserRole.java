package Enums;

public enum UserRole {
    DOCTOR("doctor"),
    ADMINISTRATOR("administrator"),
    PHARMACIST("pharmacist"),
    PATIENT("patient");

    private final String value;

    // Constructor to assign the lowercase value
    UserRole(String value) {
        this.value = value;
    }

    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }

    // Getter for the lowercase value (optional if needed directly)
    public String getValue() {
        return value;
    }
}
