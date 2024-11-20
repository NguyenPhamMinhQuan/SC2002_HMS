package Models;

import Enums.AppointmentStatus;

import java.util.Date;

/**
 * Represents an appointment between a patient and a doctor.
 * This class stores the details about the appointment including patient and doctor IDs,
 * appointment status, date, and the appointment outcome (if completed).
 */
public class Appointment {

    private int id;               // Appointment ID
    private final String patientId; // Patient's ID (unchanged from the old code)
    private String doctorId;       // Doctor's ID
    private AppointmentStatus status; // Appointment status (confirmed, canceled, completed, etc.)
    private String date;           // Appointment date (in String format for simplicity, can be Date type)
    private AppointmentOutcomeRecord outcomeRecord; // Only set for completed appointments

    /**
     * Constructs an Appointment object with the specified details.
     *
     * @param id                the unique ID of the appointment
     * @param patientId         the unique ID of the patient
     * @param doctorId          the unique ID of the doctor
     * @param status            the status of the appointment (e.g., "confirmed", "canceled", "completed")
     * @param date              the date and time of the appointment (as String, can be Date if needed)
     */
    public Appointment(int id, String patientId, String doctorId, String date, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
        this.outcomeRecord = null; // Outcome record is only set for completed appointments
    }

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AppointmentOutcomeRecord getOutcomeRecord() {
        return outcomeRecord;
    }

    public void setOutcomeRecord(AppointmentOutcomeRecord outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }

    // Method to check if the appointment has an outcome (i.e., is completed)
    public boolean hasOutcome() {
        return outcomeRecord != null;
    }
}
