package Models;

/**
 * Represents a medication prescribed during an appointment.
 * This class holds the medication name and its status (e.g., pending, completed).
 */
public class Medication {

    private String medicationName;
    private String status; // e.g., "pending", "completed"

    /**
     * Constructs a Medication object with the specified name and status.
     *
     * @param medicationName the name of the medication
     * @param status         the status of the medication (e.g., "pending")
     */
    public Medication(String medicationName, String status) {
        this.medicationName = medicationName;
        this.status = status;
    }

    // Getter and Setter methods

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
