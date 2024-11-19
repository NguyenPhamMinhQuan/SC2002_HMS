package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the outcome of a completed appointment, including details like service type,
 * prescribed medications, and consultation notes.
 */
public class AppointmentOutcomeRecord {

    private int appointmentID; // Unique appointment ID
    private String appointmentDate;
    private String serviceType; // e.g., consultation, X-ray, etc.
    private List<Medication> prescribedMedications;
    private String consultationNotes;
    private boolean dispensed;

    public AppointmentOutcomeRecord(int appointmentID, String appointmentDate, String serviceType, String consultationNotes, boolean dispensed) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.prescribedMedications = new ArrayList<>();
        this.consultationNotes = consultationNotes;
        this.dispensed = dispensed;
    }

    // Add a medication to the list of prescribed medications
    public void addMedication(Medication medication) {
        prescribedMedications.add(medication);
    }

    // Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Medication> getPrescribedMedications() {
        return prescribedMedications;
    }

    public void setPrescribedMedications(List<Medication> prescribedMedications) {
        this.prescribedMedications = prescribedMedications;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public boolean isDispensed() {
        return dispensed;
    }

    public void setDispensed(boolean dispensed) {
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