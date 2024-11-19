package Models;

import Enums.AppointmentStatus;

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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
