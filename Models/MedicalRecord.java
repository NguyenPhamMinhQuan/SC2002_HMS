package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a patient's medical record in the Hospital Management System.
 * This class holds all personal and medical information related to the patient.
 */
public class MedicalRecord {

    private final String patientID;
    private String bloodType;
    private final List<Diagnosis> pastDiagnoses;
    private final List<AppointmentOutcomeRecord> appointmentOutcomes;

    // Personal Information
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;

    /**
     * Default constructor to create an empty medical record.
     * Initializes all lists and fields to default values.
     *
     * @param patientID the unique ID of the patient.
     */
    public MedicalRecord(String patientID) {
        this.patientID = patientID;
        this.bloodType = "";
        this.pastDiagnoses = new ArrayList<>();
        this.appointmentOutcomes = new ArrayList<>();
        this.dateOfBirth = "";
        this.phoneNumber = "";
        this.emailAddress = "";
    }

    // Getter methods for private fields

    public String getPatientID() {
        return patientID;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    // Setter methods with validation
    public void setBloodType(String bloodType) {
        if (bloodType.matches("^(A|B|AB|O)[+-]$")) {
            this.bloodType = bloodType;
        } else {
            throw new IllegalArgumentException("Invalid blood type. Use format: A+, A-, B+, etc.");
        }
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{8,10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number. Must contain 10-15 digits.");
        }
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            this.emailAddress = emailAddress;
        } else {
            throw new IllegalArgumentException("Invalid email address format.");
        }
    }

    // Diagnosis Management

    /**
     * Adds a new diagnosis to the medical record.
     *
     * @param diagnosis the diagnosis to add.
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        pastDiagnoses.add(diagnosis);
    }

    /**
     * Retrieves all diagnoses in the medical record.
     * Returns an unmodifiable list to prevent external modification.
     *
     * @return a list of all past diagnoses.
     */
    public List<Diagnosis> getPastDiagnoses() {
        return Collections.unmodifiableList(pastDiagnoses);
    }

    // Appointment Outcome Management

    /**
     * Adds an appointment outcome to the medical record.
     *
     * @param outcome the appointment outcome to add.
     */
    public void addAppointmentOutcome(AppointmentOutcomeRecord outcome) {
        appointmentOutcomes.add(outcome);
    }

    /**
     * Retrieves all appointment outcomes.
     * Returns an unmodifiable list to prevent external modification.
     *
     * @return a list of appointment outcomes.
     */
    public List<AppointmentOutcomeRecord> getAppointmentOutcomes() {
        return Collections.unmodifiableList(appointmentOutcomes);
    }

    // Display and Summary Methods

    /**
     * Displays the patient's medical record, including personal information,
     * diagnoses, and appointment outcomes.
     */
    public void displayMedicalRecord() {
        System.out.println("Medical Record for Patient ID: " + patientID);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + emailAddress);
        System.out.println("Blood Type: " + bloodType);

        // Display diagnoses
        System.out.println("\nDiagnoses and Treatments:");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-25s %-20s\n", "Diagnosis", "Date", "Prescription", "Prescription Status");
        System.out.println("-------------------------------------------------------------------------------");

        for (Diagnosis diagnosis : pastDiagnoses) {
            System.out.printf("%-20s %-15s %-25s %-20s\n",
                    diagnosis.getCondition(),
                    diagnosis.getDiagnosisDate(),
                    diagnosis.getPrescription(),
                    diagnosis.getPrescriptionStatus());
        }

        // Display appointment outcomes
        System.out.println("\nAppointment Outcomes:");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-25s %-20s\n", "Date", "Service Type", "Prescribed Medications", "Notes");
        System.out.println("-------------------------------------------------------------------------------");

        for (AppointmentOutcomeRecord outcome : appointmentOutcomes) {
            String medications = outcome.getPrescribedMedications().stream()
                    .map(med -> med.getMedicineName() + " (" + med.getStockLevel() + ")")
                    .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

            System.out.printf("%-15s %-20s %-25s %-20s\n",
                    outcome.getAppointmentDate(),
                    outcome.getServiceType(),
                    medications,
                    outcome.getConsultationNotes());
        }

        System.out.println("-------------------------------------------------------------------------------");
    }

    /**
     * Retrieves the most recent diagnosis.
     *
     * @return the latest diagnosis, or null if no diagnoses exist.
     */
    public Diagnosis getMostRecentDiagnosis() {
        if (pastDiagnoses.isEmpty()) return null;
        return pastDiagnoses.get(pastDiagnoses.size() - 1);
    }

    /**
     * Retrieves the most recent appointment outcome.
     *
     * @return the latest appointment outcome, or null if none exist.
     */
    public AppointmentOutcomeRecord getMostRecentAppointmentOutcome() {
        if (appointmentOutcomes.isEmpty()) return null;
        return appointmentOutcomes.get(appointmentOutcomes.size() - 1);
    }

    /**
     * Displays all appointment outcomes for this patient.
     */
    public void displayAppointmentOutcomes() {
        if (appointmentOutcomes.isEmpty()) {
            System.out.println("No appointment outcomes recorded for this patient.");
            return;
        }

        System.out.println("\nAppointment Outcomes for Patient ID: " + patientID);
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-25s %-20s\n", "Date", "Service Type", "Prescribed Medications", "Notes");
        System.out.println("-------------------------------------------------------------------------------");

        for (AppointmentOutcomeRecord outcome : appointmentOutcomes) {
            String medications = outcome.getPrescribedMedications().stream()
                    .map(med -> med.getMedicineName() + " (" + med.getStockLevel() + ")")
                    .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

            System.out.printf("%-15s %-20s %-25s %-20s\n",
                    outcome.getAppointmentDate(),
                    outcome.getServiceType(),
                    medications,
                    outcome.getConsultationNotes());
        }

        System.out.println("-------------------------------------------------------------------------------");
    }

}
