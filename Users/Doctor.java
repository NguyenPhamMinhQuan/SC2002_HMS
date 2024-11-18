package Users;

import Models.Appointment;
import Models.Diagnosis;
import Models.MedicalRecord;
import Models.User;
import Systems.AppointmentSystem;
import Systems.InputHandler;
import Systems.MedicalRecordSystem;
import Systems.UserManagementSystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Represents a doctor in the hospital management system.
 * Inherits from User class.
 */
public class Doctor extends User {

    /**
     * Constructs a new Doctor.
     *
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female).
     * @param age      the age of the user.
     */
    public Doctor(String userId, String password, String name, String gender, int age) {
        super(userId, password, "doctor", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> viewMedicalRecord();
            case 2 -> updateMedicalRecord();
            case 3 -> viewPersonalSchedule();
            case 4 -> setAvailability();
            case 5 -> manageAppointmentRequests();
            case 6 -> viewUpcomingAppointments();
            case 7 -> recordAppointmentOutcome();
            case 8 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Choose the interested patient by validating the input against the available patients.
     *
     * @return userID of the patient.
     */
    public String choosePatient() {
        System.out.println("Please choose among the available Patients:");
        UserManagementSystem.filterPatients();

        System.out.print("PatientID chosen: ");
        String patientID = InputHandler.nextLine();

        if (UserManagementSystem.users.containsKey(patientID)) {
            User chosenUser = UserManagementSystem.users.get(patientID);

            if (chosenUser.getRole().equalsIgnoreCase("patient")) {
                System.out.println("You have chosen Patient: " + chosenUser.getName());
                return patientID;
            } else {
                System.out.println("The chosen ID does not belong to a patient. Please choose a valid patient ID.");
            }
        } else {
            System.out.println("The Patient ID does not exist. Please try again.");
        }
        return choosePatient(); // Retry if invalid
    }

    /**
     * Views a patient's medical record.
     */
    public void viewMedicalRecord() {
        System.out.println("Viewing patient medical records...");
        String patientID = choosePatient();

        MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
        if (medicalRecord != null) {
            medicalRecord.displayMedicalRecord();
        } else {
            System.out.println("Medical record for Patient ID " + patientID + " not found.");
        }
    }

    /**
     * Updates a patient's medical record.
     */
    public void updateMedicalRecord() {
        String patientID = choosePatient();

        System.out.println("What would you like to update?");
        System.out.println("1. Blood Type");
        System.out.println("2. Add Diagnosis");
        int choice = InputHandler.nextInt();

        if (choice == 1) {
            System.out.print("Enter the updated Blood Type: ");
            String bloodType = InputHandler.nextLine();
            MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
            if (medicalRecord != null) {
                medicalRecord.setBloodType(bloodType);
                MedicalRecordSystem.saveMedicalRecord(medicalRecord);
                System.out.println("Blood Type updated successfully.");
            } else {
                System.out.println("Failed to update Blood Type. Medical record not found.");
            }
        } else if (choice == 2) {
            System.out.print("Enter Diagnosis Condition: ");
            String condition = InputHandler.nextLine();

            String diagnosisDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            System.out.print("Enter Prescription: ");
            String prescription = InputHandler.nextLine();

            System.out.print("Enter Prescription Status: ");
            String status = InputHandler.nextLine();

            MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
            if (medicalRecord != null) {
                Diagnosis diagnosis = new Diagnosis(condition, diagnosisDate, prescription, status);
                medicalRecord.addDiagnosis(diagnosis);
                MedicalRecordSystem.saveMedicalRecord(medicalRecord);
                System.out.println("Diagnosis added successfully.");
            } else {
                System.out.println("Failed to add Diagnosis. Medical record not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the doctor's personal schedule, including availability and scheduled appointments.
     */
    public void viewPersonalSchedule() {
        List<String> availability = AppointmentSystem.getDoctorAvailability(getUserId());
        if (availability != null && !availability.isEmpty()) {
            System.out.println("Available Slots:");
            for (String slot : availability) {
                System.out.println("  " + slot);
            }
        } else {
            System.out.println("No available slots set.");
        }
        AppointmentSystem.displayAppointmentsByDoctor(getUserId(), null);
    }

    /**
     * Sets the availability for appointments.
     */
    public void setAvailability() {
        AppointmentSystem.setAvailability(getUserId());
    }

    /**
     * Manages appointment requests (accept or decline).
     */
    public void manageAppointmentRequests() {
        List<Appointment> pendingAppointments = AppointmentSystem.getAppointmentsByDoctor(getUserId(), "pending");

        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointment requests.");
            return;
        }

        for (Appointment appointment : pendingAppointments) {
            System.out.println("Appointment ID: " + appointment.getID());
            System.out.println("Patient ID: " + appointment.getPatientID());
            System.out.println("Date: " + AppointmentSystem.formatDate(appointment.getAppointmentDate()));
            System.out.print("Accept or Decline (A/D)? ");
            String decision = InputHandler.nextLine();

            if (decision.equalsIgnoreCase("A")) {
                AppointmentSystem.updateAppointmentStatus(appointment.getID(), "confirmed");
                System.out.println("Appointment confirmed.");
            } else if (decision.equalsIgnoreCase("D")) {
                AppointmentSystem.updateAppointmentStatus(appointment.getID(), "declined");
                System.out.println("Appointment declined.");
            } else {
                System.out.println("Invalid input. Skipping appointment.");
            }
        }
    }

    /**
     * View upcoming confirmed appointments.
     */
    public void viewUpcomingAppointments() {
        AppointmentSystem.displayAppointmentsByDoctor(getUserId(), "confirmed");
    }

    private void recordAppointmentOutcome() {
        // Logic for recording appointment outcomes
        System.out.println("Recording appointment outcome...");
        // TODO: Implement functionality
    }
}
