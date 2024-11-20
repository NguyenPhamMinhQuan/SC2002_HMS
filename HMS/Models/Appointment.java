package HMS.Models;

import HMS.Enums.AppointmentStatus;

import java.util.Date;

/**
 * Represents an appointment between a patient and a doctor.
 * This class stores the details about the appointment including patient and doctor IDs,
 * appointment status, date, and the appointment outcome (if completed).
 */
public class Appointment {

    private int ID;
    private final String patientID;
    private String doctorID;
    private AppointmentStatus appointmentStatus; // e.g., "confirmed", "canceled", "completed"
    private Date appointmentDate;
    private final AppointmentOutcomeRecord outcomeRecord;

    /**
     * Constructs an Appointment object with the specified details.
     *
     * @param ID                the unique ID of the appointment
     * @param patientID         the unique ID of the patient
     * @param doctorID          the unique ID of the doctor
     * @param appointmentStatus the status of the appointment (e.g., "confirmed", "canceled")
     * @param appointmentDate   the date and time of the appointment
     */
    public Appointment(int ID, String patientID, String doctorID, AppointmentStatus appointmentStatus, Date appointmentDate) {
        this.ID = ID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentStatus = appointmentStatus;
        this.appointmentDate = appointmentDate;
        this.outcomeRecord = null; // Outcome record is only set for completed appointments
    }

    /**
     * get the unique identifier of the appointment.
     *
     * @return the Appointment ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique ID of the appointment.
     *
     * @param ID the new appointment ID.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets the unique ID of the patient.
     *
     * @return the patient ID.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the unique ID of the doctor.
     *
     * @return the doctor ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Sets the unique ID of the doctor.
     *
     * @param doctorID the new doctor ID.
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Gets the current status of the appointment.
     *
     * @return the appointment status.
     */
    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    /**
     * Updates the status of the appointment.
     *
     * @param appointmentStatus the new appointment status.
     */
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    /**
     * Gets the date and time of the appointment.
     *
     * @return the appointment date.
     */
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Updates the date and time of the appointment.
     *
     * @param appointmentDate the new appointment date.
     */
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
