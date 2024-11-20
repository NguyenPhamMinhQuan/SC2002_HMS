package Models;

import Enums.Dispensed;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the outcome of a completed appointment, including details like service type,
 * prescribed medications, and consultation notes.
 */
public class AppointmentOutcomeRecord {

    private final List<Medication> prescribedMedications;
    private final String consultationNotes;
    private final int appointmentID; // Unique appointment ID
    private final String appointmentDate;
    private final String serviceType; // e.g., consultation, X-ray, etc.
    private Dispensed dispensed;
    private final String doctorID;
    private final String patientID;

    /**
     * Constructs a new {@code AppointmentOutcomeRecord} with the specified details.
     *
     * @param appointmentID Unique identifier for the appointment
     * @param appointmentDate   Date of the appointment.
     * @param serviceType   Type of service provided.
     * @param consultationNotes Doctor's notes about the appointment
     * @param dispensed Status of medication dispensation.
     * @param doctorID  Unique identifier of the doctor.
     * @param patientID unique identifier of the patient.
     */
    public AppointmentOutcomeRecord(int appointmentID, String appointmentDate, String serviceType, String consultationNotes, Dispensed dispensed, String doctorID, String patientID) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.prescribedMedications = new ArrayList<>();
        this.consultationNotes = consultationNotes;
        this.dispensed = dispensed;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    /**
     * Gets the unique ID of the doctor who handled the appointment.
     *
     * @return the doctor's ID.
     */
    public String getDoctorID() { return doctorID; }

    /**
     * Gets the unique ID of the patient involved in the appointment.
     *
     * @return the patient's ID.
     */
    public String getPatientID() { return patientID; }

    /**
     * Adds a medication to the list of prescribed medications.
     *
     * @param medication the medication to add.
     */
    public void addMedication(Medication medication) {
        prescribedMedications.add(medication);
    }

    /**
     * Gets the unique ID of the appointment.
     *
     * @return the appointment ID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return the appointment date.
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Gets the type of service provided during the appointment.
     *
     * @return the service type.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Gets the list of medications prescribed during the appointment.
     *
     * @return a list of prescribed medications.
     */
    public List<Medication> getPrescribedMedications() {
        return prescribedMedications;
    }

    /**
     * Gets the consultation notes from the appointment.
     *
     * @return the consultation notes.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Gets the medication dispensation status.
     *
     * @return the dispensation status.
     */
    public Dispensed isDispensed() {
        return dispensed;
    }

    /**
     * Sets the medication dispensation status.
     *
     * @param dispensed the new dispensation status.
     */
    public void setDispensed(Dispensed dispensed) {
        this.dispensed = dispensed;
    }


    /**
     * Formats the list of prescribed medications as a single string.
     * The format includes medication names and quantities, separated by semicolons.
     *
     * @return a formatted string representing prescribed medications.
     */
    public String getMedicationsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Medication medication : prescribedMedications) {
            sb.append(medication.getMedicationName())
                    .append(" (")
                    .append(medication.getQuantity())
                    .append(")")
                    .append("; ");
        }
        return sb.toString().trim();
    }
}
