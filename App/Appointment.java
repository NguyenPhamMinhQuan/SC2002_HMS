import java.util.Date;

/**
 * Represents an appointment between a patient and a doctor.
 * This class stores the details about the appointment including patient and doctor IDs,
 * appointment status, date, and the appointment outcome (if completed).
 */
public class Appointment {

    private String patientID;
    private String doctorID;
    private String appointmentStatus; // e.g., "confirmed", "canceled", "completed"
    private Date appointmentDate;
    private AppointmentOutcomeRecord outcomeRecord;

    /**
     * Constructs an Appointment object with the specified details.
     *
     * @param patientID the unique ID of the patient
     * @param doctorID the unique ID of the doctor
     * @param appointmentStatus the status of the appointment (e.g., "confirmed", "canceled")
     * @param appointmentDate the date and time of the appointment
     */
    public Appointment(String patientID, String doctorID, String appointmentStatus, Date appointmentDate) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentStatus = appointmentStatus;
        this.appointmentDate = appointmentDate;
        this.outcomeRecord = null; // Outcome record is only set for completed appointments
    }

    // Getter and Setter methods
    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentOutcomeRecord getOutcomeRecord() {
        return outcomeRecord;
    }

    public void setOutcomeRecord(AppointmentOutcomeRecord outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }

    public void setAppointmentStatus(String status) {
        this.appointmentStatus = status;
    }
}
