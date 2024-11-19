package Enums;

public enum AppointmentStatus {
    PENDING("pending"), // Just created
    CANCELLED("cancelled"),
    APPROVED("approved"), // Doctor approved, has not been checked
    COMPLETED("completed"); // Patient checked (at this point, there is an outcome)

    private final String value;

    // Constructor to assign the lowercase value
    AppointmentStatus(String value) {
        this.value = value;
    }

    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }
}