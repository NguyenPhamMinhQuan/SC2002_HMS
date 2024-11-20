package HMS.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a patient's medical record in the Hospital Management System.
 * This class is focused on holding the patient's personal and medical HMS.data.
 */
public class MedicalRecord {

    private final String patientID;
    private final List<Diagnosis> diagnoses;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String bloodType;

    /**
     * Constructs a new {@code MedicalRecord} instance for the specified patient ID.
     *
     * @param patientID the unique identifier for the patient.
     */
    public MedicalRecord(String patientID) {
        this.patientID = patientID;
        this.diagnoses = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the patient
     *
     * @return patient's ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the patient's date of birth.
     *
     * @return the date of birth.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the patient's date of birth.
     *
     * @param dateOfBirth the date of birth in a valid format (e.g., YYYY-MM-DD).
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the patient's phone number.
     *
     * @return the phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the patient's phone number.
     *
     * @param phoneNumber the phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the patient's email address.
     *
     * @return the email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the patient's email address.
     *
     * @param emailAddress the email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the patient's blood type.
     *
     * @return the blood type.
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Sets the patient's blood type.
     *
     * @param bloodType the blood type to set (e.g., "A+", "O-").
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Gets an unmodifiable list of the patient's diagnoses.
     *
     * @return the list of diagnoses.
     */
    public List<Diagnosis> getDiagnoses() {
        return Collections.unmodifiableList(diagnoses);
    }

    /**
     * Adds a list of diagnoses to the patient's medical record.
     *
     * @param diagnoses the list of diagnoses to add.
     */
    public void addDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses.addAll(diagnoses);
    }
}