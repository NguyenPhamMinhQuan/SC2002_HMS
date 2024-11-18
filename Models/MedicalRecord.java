package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient's medical record in the Hospital Management System.
 * This class holds all personal and medical information related to the patient.
 */
public class MedicalRecord {

    private final String patientID;
    private String bloodType;
    private final List<Diagnosis> pastDiagnoses;

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

    // Setter methods for mutable fields
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

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
     *
     * @return a list of all past diagnoses.
     */
    public List<Diagnosis> getPastDiagnoses() {
        return pastDiagnoses;
    }

    /**
     * Displays the patient's medical record, including personal information
     * and a detailed table of diagnoses with associated details.
     */
    public void displayMedicalRecord() {
        System.out.println("Medical Record for Patient ID: " + patientID);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + emailAddress);
        System.out.println("Blood Type: " + bloodType);

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
        System.out.println("-------------------------------------------------------------------------------");
    }
}
