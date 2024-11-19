package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a patient's medical record in the Hospital Management System.
 * This class is focused on holding the patient's personal and medical data.
 */
public class MedicalRecord {

    private final String patientID;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String bloodType;
    private final List<Diagnosis> diagnoses;

    /**
     * Constructs a MedicalRecord for a specific patient.
     *
     * @param patientID the unique ID of the patient.
     */
    public MedicalRecord(String patientID) {
        this.patientID = patientID;
        this.diagnoses = new ArrayList<>();
    }

    // Getters and setters
    public String getPatientID() {
        return patientID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<Diagnosis> getDiagnoses() {
        return Collections.unmodifiableList(diagnoses);
    }

    public void addDiagnosis(Diagnosis diagnosis) {
        this.diagnoses.add(diagnosis);
    }

    public void addDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses.addAll(diagnoses);
    }
}