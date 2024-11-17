

/**
 * Represents a diagnosis with detailed medical information.
 */
class Diagnosis {
    private String condition;
    private String diagnosisDate;
    private String prescription;
    private String prescriptionStatus;

    /**
     * Constructor to initialize a diagnosis.
     * 
     * @param condition           the diagnosed condition.
     * @param diagnosisDate       the date of diagnosis.
     * @param prescription        the requested prescription.
     * @param prescriptionStatus  the status of the prescription request.
     */
    public Diagnosis(String condition,String diagnosisDate, String prescription,String prescriptionStatus) {
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

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
}
