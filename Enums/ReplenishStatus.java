package Enums;

/**
 * Enum representing the various statuses for a replenishment request in the hospital management system.
 * Each status is associated with a string value that represents its state.
 */
public enum ReplenishStatus {
    PENDING("pending"),
    APPROVED("approved"),
    COMPLETED("completed"),
    REJECTED("rejected");

    private final String value;

    /**
     * Constructor for the enum, assigning a string value to each status.
     *
     * @param value the string value representing the replenishment request status (e.g., "pending", "approved")
     */
    // Constructor to assign the lowercase value
    ReplenishStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the replenishment request status.
     * The string is returned in lowercase format.
     *
     * @return the lowercase value of the replenishment status (e.g., "pending")
     */
    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }
}
