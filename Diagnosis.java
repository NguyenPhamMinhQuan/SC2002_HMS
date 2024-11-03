import java.util.List;


public class Diagnosis {
	private String disease;
    private String severity;
    private String treatment;
    private List<String> prescriptions;
    private int doctorID;
    private String doctorName;
    private String outcome;
    
    public Diagnosis(String disease, String severity, String treatment, List<String> prescriptions, int doctorID, String doctorName, String outcome) {
        this.disease = disease;
        this.severity = severity;
        this.treatment = treatment;
        this.prescriptions = prescriptions;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.outcome = outcome;
    }
    
    // getters
    public String getDisease() {
        return disease;
    }

    public String getSeverity() {
        return severity;
    }

    public String getTreatment() {
        return treatment;
    }

    public List<String> getPrescriptions() {
        return prescriptions;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getOutcome() {
        return outcome;
    }
    
    // to view whole diagnosis
    public String toString() {
        return "Diagnosis{" +
                "disease='" + disease + '\'' +
                ", severity='" + severity + '\'' +
                ", treatment='" + treatment + '\'' +
                ", prescriptions=" + prescriptions +
                ", doctorID=" + doctorID +
                ", doctorName='" + doctorName + '\'' +
                ", outcome='" + outcome + '\'' +
                '}';
    }

}
