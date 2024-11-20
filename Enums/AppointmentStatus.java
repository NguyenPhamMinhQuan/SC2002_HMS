package Enums;

/**
 * Enum representing the various statuses an appointment can have.
 */
public enum AppointmentStatus {
    PENDING("pending"), // Just created
    CANCELLED("cancelled"),
    APPROVED("approved"), // Doctor approved, has not been checked
    COMPLETED("completed"); // Patient checked (at this point, there is an outcome)

    private final String value;

    /**
     * Constructor to assign the lowercase value to each status.
     *
     * @param value the lowercase string representing the status of the appointment
     */
    // Constructor to assign the lowercase value
    AppointmentStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the appointment status.
     *
     * @return the status as a lowercase string
     */
    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }
}
