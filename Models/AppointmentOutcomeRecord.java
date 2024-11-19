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
    private int appointmentID; // Unique appointment ID
    private String appointmentDate;
    private String serviceType; // e.g., consultation, X-ray, etc.
    private Dispensed dispensed;
    private String doctorID;
    private String patientID;

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

    public String getDoctorID() { return doctorID; }

    public String getPatientID() { return patientID; }

    public void addMedication(Medication medication) {
        prescribedMedications.add(medication);
    }

    public int getAppointmentID() {
        return appointmentID;
    }

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

    public Dispensed isDispensed() {
        return dispensed;
    }

    public void setDispensed(Dispensed dispensed) {
        this.dispensed = dispensed;
    }

    // Format medications as a single string for CSV saving
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