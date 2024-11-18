package Users;

import Models.*;
import Systems.InputHandler;
import Systems.UserManagementSystem;

import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

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
            case 1 -> {
                System.out.println("Viewing patient medical records...");
                String patientID = choosePatient();
                viewMedicalRecord(patientID);
            }
            case 2 -> {
                System.out.println("Updating patient medical records...");
                String patientID = choosePatient();

                System.out.println("What would you like to update?");
                System.out.println("1. Blood Type");
                System.out.println("2. Add Diagnosis");
                int choice = InputHandler.nextInt();
                if (choice == 1) {
                    updateBloodType(patientID);
                } else if (choice == 2) {
                    addDiagnosis(patientID);
                } else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            case 3 -> {
                System.out.println("Viewing personal schedule...");
                // Shows all appointments regardless of status
//                AppointmentSystem.displayAppointmentsByDoctorpointmentsByDoctor(getUserId(), null);
            }
            case 4 -> {
                System.out.println("Setting availability for appointments...");
//                Models.AppointmentSystem.setAvailability(getUserId());
            }

            case 5 -> {
//                System.out.println("Viewing all scheduled appointments...");
//                // Retrieve the doctor's confirmed appointments
//                List<Appointment> doctorAppointments = AppointmentSystem.getAppointmentsByDoctor(getUserId(), "confirmed");
//
//                if (doctorAppointments.isEmpty()) {
//                    System.out.println("You have no scheduled appointments.");
//                } else {
//                    // Display all scheduled appointments
//                    System.out.println("Your scheduled appointments:");
//                    int index = 1;
//                    for (Appointment appointment : doctorAppointments) {
//                        System.out.println(index + ". Appointment ID: " + appointment.getID()
//                                + ", Patient ID: " + appointment.getPatientID()
//                                + ", Date: " + AppointmentSystem.formatDate(appointment.getAppointmentDate()));
//                        index++;
//                    }
//
//                    // Allow the doctor to choose an appointment to cancel
//                    System.out.print("Enter the number of the appointment to cancel (0 to exit): ");
//                    int choice = InputHandler.nextInt();
//
//                    if (choice == 0) {
//                        System.out.println("Cancellation process aborted.");
//                    } else if (choice > 0 && choice <= doctorAppointments.size()) {
//                        Appointment appointmentToCancel = doctorAppointments.get(choice - 1);
//                        AppointmentSystem.cancelAppointment(appointmentToCancel.getID());
//                        System.out.println("Appointment ID " + appointmentToCancel.getID() + " has been canceled.");
//                    } else {
//                        System.out.println("Invalid choice. Please try again.");
//                    }
//                }
            }


            case 6 -> {
                System.out.println("Viewing upcoming appointments...");
//                AppointmentSystem.displayAppointmentsByDoctor(getUserId(), "pending");

            }
            case 7 -> {
                System.out.println("Recording appointment outcome...");
//                appointmentOutcome = new AppointmentOutcome();
//                Models.AppointmentSystem.addAppointmentOutcome(getUserId());

            }
            case 8 -> {
                System.out.println("Logging out...");
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

        // Filter and display patients only
        UserManagementSystem.filterPatients();

        System.out.print("PatientID chosen: ");
        String patientID = InputHandler.nextLine();

        // Check if the patientID exists in the users map and if it's a patient
        if (UserManagementSystem.users.containsKey(patientID)) {
            User chosenUser = UserManagementSystem.users.get(patientID);

            if (chosenUser.getRole().equalsIgnoreCase("patient")) {
                System.out.println("You have chosen Patient: " + chosenUser.getName());
                return patientID;
            } else {
                System.out.println("The chosen ID does not belong to a patient. Please choose a valid patient ID.");
                return choosePatient();  // Recursively ask again if the ID doesn't belong to a patient
            }
        } else {
            System.out.println("The Patient ID does not exist. Please try again.");
            return choosePatient();  // Recursively ask again if the ID doesn't exist
        }
    }

    /**
     * Views a patient's medical record.
     *
     * @param medicalRecord the medical record to view.
     */
    public void viewMedicalRecord(String patientID) {
        MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
        medicalRecord.displayMedicalRecord();
    }

    /**
     * Updates the blood type in a patient's medical record and save it
     *
     * @param patientID the userID of the patient chosen.
     */
    public void updateBloodType(String patientID) {
        Scanner scanner = new Scanner(System.in);
        MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
        System.out.print("Enter patient's updated BloodType: ");
        String newBloodType = scanner.next();
        medicalRecord.setBloodType(newBloodType);
        System.out.println("Blood type updated to " + newBloodType + " for patient ID: " + medicalRecord.getPatientID());
        System.out.println("Please review the updated changes: ");
        medicalRecord.displayMedicalRecord();
        MedicalRecordSystem.saveMedicalRecord(medicalRecord);
        scanner.close();
    }

    /**
     * Adds a new diagnosis to a patient's medical record.
     *
     * @param patientID the userID of the chosen patient
     */
    public void addDiagnosis(String patientID) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Prompt for diagnosis condition
            System.out.print("Enter Diagnosis Condition: ");
            String diagnosisCondition = scanner.nextLine();

            // Get today's date
            Date today = new Date();

            // Format the date as desired
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String diagnosisDate = dateFormat.format(today);

            // Prompt for prescription
            System.out.print("Enter Prescription: ");
            String prescription = scanner.nextLine();

            // Prompt for prescription status
            System.out.print("Enter Prescription Status: ");
            String prescriptionStatus = scanner.nextLine();
            MedicalRecord medicalRecord = MedicalRecordSystem.loadMedicalRecord(patientID);
            Diagnosis diagnosis = new Diagnosis(diagnosisCondition, diagnosisDate, prescription, prescriptionStatus);
            medicalRecord.addDiagnosis(diagnosis);
            System.out.println("Added diagnosis: " + diagnosisCondition + " for patient ID: " + medicalRecord.getPatientID());
            medicalRecord.displayMedicalRecord();
            MedicalRecordSystem.saveMedicalRecord(medicalRecord);
        } catch (Exception e) {
            System.out.println("An error occurred while processing input: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
