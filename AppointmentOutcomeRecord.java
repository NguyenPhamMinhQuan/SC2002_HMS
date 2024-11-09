import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentOutcomeRecord {
    private int appointmentID;
    private int patientID;
    private int doctorID;
    private Date appointmentDate;
    private String typeOfService;
    private List<String> prescribedMedications = new ArrayList<>();
    private String prescriptionStatus;
    private String consultationNotes;

    // Constructor
    public AppointmentOutcomeRecord(int appointmentID, int patientID, int doctorID, Date appointmentDate, String typeOfService) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentDate = appointmentDate;
        this.typeOfService = typeOfService;
        this.prescriptionStatus = "Pending";  // Default status
    }

    // Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    public List<String> getPrescribedMedications() {
        return prescribedMedications;
    }

    public void addPrescribedMedication(String medication) {
        this.prescribedMedications.add(medication);
    }

    public String getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    // Method to update consultation notes
    public void updateConsultationNotes(String notes) {
        this.consultationNotes = notes;
    }

    // Method to display Appointment Outcome Record details
    @Override
    public String toString() {
        return "AppointmentOutcomeRecord{" +
                "appointmentID=" + appointmentID +
                ", patientID=" + patientID +
                ", doctorID=" + doctorID +
                ", appointmentDate=" + appointmentDate +
                ", typeOfService='" + typeOfService + '\'' +
                ", prescribedMedications=" + prescribedMedications +
                ", prescriptionStatus='" + prescriptionStatus + '\'' +
                ", consultationNotes='" + consultationNotes + '\'' +
                '}';
    }
}
