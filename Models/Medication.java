package Models;

/**
 * Represents a medication prescribed during an appointment.
 * This class holds the medication name and its status (e.g., pending, completed).
 */
public class Medication {

    private final String medicationName;
    private final Integer quantity;
    private String status; // e.g., "pending", "completed"

    public Medication(String medicationName, String status, Integer quantity) {
        this.medicationName = medicationName;
        this.status = status;
        this.quantity = quantity;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
