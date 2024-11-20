package HMS.Models;

/**
 * Represents a medication prescribed during an appointment.
 * This class holds the medication name and its status (e.g., pending, completed).
 */
public class Medication {

    private final String medicationName;
    private final Integer quantity;
    private String status; // e.g., "pending", "completed"

    /**
     * Constructs a new {@code Medication} instance
     *
     * @param medicationName    The name of the Medication.
     * @param status            The status of the medicine, such as "pending or "completed"
     * @param quantity          The quantity of the medication prescribed
     */
    public Medication(String medicationName, String status, Integer quantity) {
        this.medicationName = medicationName;
        this.status = status;
        this.quantity = quantity;
    }

    /**
     * Gets the name of the prescribed medication.
     *
     * @return the medication name.
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Gets the current status of the medication.
     *
     * @return the medication status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status of the medication.
     *
     * @param status the new status of the medication, such as "pending" or "completed".
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the quantity of the medication prescribed.
     *
     * @return the prescribed quantity of the medication.
     */
    public Integer getQuantity() {
        return quantity;
    }
}
