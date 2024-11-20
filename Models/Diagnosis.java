package Models;

/**
 * Represents a diagnosis with detailed medical information.
 */
public class Diagnosis {
    private final String condition;
    private final String diagnosisDate;
    private final String prescription;

    /**
     * Constructs a new {@code Diagnosis} object with the specified details.
     *
     * @param condition      the diagnosed medical condition.
     * @param diagnosisDate  the date of the diagnosis in a valid format (e.g., YYYY-MM-DD).
     * @param prescription   the prescription recommended for the condition.
     */
    public Diagnosis(String condition, String diagnosisDate, String prescription) {
        this.condition = condition;
        this.diagnosisDate = diagnosisDate;
        this.prescription = prescription;
    }

    /**
     * Gets the diagnosed medical condition.
     *
     * @return the condition as a string.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets the date when the diagnosis was made.
     *
     * @return the diagnosis date as a string.
     */
    public String getDiagnosisDate() {
        return diagnosisDate;
    }

    /**
     * Gets the prescription recommended for the diagnosed condition.
     *
     * @return the prescription as a string.
     */
    public String getPrescription() {
        return prescription;
    }
}
