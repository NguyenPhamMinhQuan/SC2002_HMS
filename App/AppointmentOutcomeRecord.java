import java.util.ArrayList;
import java.util.List;

/**
 * Represents the outcome of a completed appointment, including details like service type,
 * prescribed medications, and consultation notes.
 */
public class AppointmentOutcomeRecord {

    private String appointmentDate;
    private String serviceType; // e.g., consultation, X-ray, etc.
    private List<Medication> prescribedMedications;
    private String consultationNotes;

    /**
     * Constructs an AppointmentOutcomeRecord with the specified details.
     *
     * @param appointmentDate the date of the appointment
     * @param serviceType the type of service provided (e.g., consultation)
     * @param consultationNotes any notes from the consultation
     */
    public AppointmentOutcomeRecord(String appointmentDate, String serviceType, String consultationNotes) {
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.prescribedMedications = new ArrayList<>();
        this.consultationNotes = consultationNotes;
    }

    // Add a medication to the list of prescribed medications
    public void addMedication(Medication medication) {
        prescribedMedications.add(medication);
    }

    // Getter and Setter methods
    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<Medication> getPrescribedMedications() {
        return prescribedMedications;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }
}
