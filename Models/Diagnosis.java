package Models;

/**
 * Represents a diagnosis with detailed medical information.
 */
public class Diagnosis {
    private final String condition;
    private final String diagnosisDate;
    private final String prescription;
    private final String prescriptionStatus;

    /**
     * Constructor to initialize a diagnosis.
     *
     * @param condition          the diagnosed condition.
     * @param diagnosisDate      the date of diagnosis.
     * @param prescription       the requested prescription.
     * @param prescriptionStatus the status of the prescription request.
     */
    public Diagnosis(String condition, String diagnosisDate, String prescription, String prescriptionStatus) {
        this.condition = condition;
        this.diagnosisDate = diagnosisDate;
        this.prescription = prescription;
        this.prescriptionStatus = prescriptionStatus;
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

    public String getPrescriptionStatus() {
        return prescriptionStatus;
    }
}
