package Models;

/**
 * Represents a diagnosis with detailed medical information.
 */
public class Diagnosis {
    private final String condition;
    private final String diagnosisDate;
    private final String prescription;

    /**
     * Constructor to initialize a diagnosis.
     *
     * @param condition          the diagnosed condition.
     * @param diagnosisDate      the date of diagnosis.
     * @param prescription       the requested prescription.
     */
    public Diagnosis(String condition, String diagnosisDate, String prescription) {
        this.condition = condition;
        this.diagnosisDate = diagnosisDate;
        this.prescription = prescription;
    }

    public String getCondition() {
        return condition;
    }

    public String getDiagnosisDate() {
        return diagnosisDate;
    }

    public String getPrescription() {
        return prescription;
    }
}
