package HMS.Enums;

/**
 * Enum representing the different user roles in the hospital management system.
 * Each role is associated with a string value that is used to represent it in the system.
 */
public enum UserRole {
    DOCTOR("doctor"),
    ADMINISTRATOR("administrator"),
    PHARMACIST("pharmacist"),
    PATIENT("patient");

    private final String value;


    /**
     * Constructor for the enum, assigning a string value to each role.
     *
     * @param value the string value representing the user role (e.g., "doctor", "administrator")
     */
    UserRole(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the user role.
     * The string is returned in lowercase format.
     *
     * @return the lowercase value of the user role (e.g., "doctor")
     */
    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }
}
