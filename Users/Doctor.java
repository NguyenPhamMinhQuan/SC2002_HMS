package Users;

import Enums.UserRole;
import Models.User;
import Systems.*;

/**
 * Represents a doctor in the hospital management system.
 * Inherits from User class.
 */
public class Doctor extends User implements UserMenuInterface {

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
        super(userId, password, UserRole.DOCTOR, name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> MedicalRecordSystem.showOrCreateMedicalRecord(
                            UserManagementSystem.selectUserIDMenu(
                                    UserManagementSystem.getUsersByRole(UserRole.PATIENT)
                            )
                    );
            case 2 -> updateMedicalRecord();
            case 3 -> {
                AppointmentSystem.displayDoctorAvailability(getUserId());
                AppointmentSystem.displayAppointmentsByDoctor(getUserId(), null);
            }
            case 4 -> setAvailability();
            case 5 -> AppointmentSystem.approvePendingAppointments(getUserId());
            case 6 -> AppointmentSystem.viewUpcomingAppointments(getUserId());
            case 7 -> AppointmentOutcomeSystem.addOutcomeByDoctor(getUserId());
            case 8 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Updates a patient's medical record.
     */
    public void updateMedicalRecord() {
        String patientID = UserManagementSystem.selectUserIDMenu(UserManagementSystem.getUsersByRole(UserRole.PATIENT));

        if (patientID == null) {
            System.out.println("No patient selected. Exiting...");
            return;
        }

        System.out.println("\nWhat would you like to update? ('exit' to exit)");
        System.out.println("1. Blood Type");
        System.out.println("2. Add Diagnosis");

        String choice = InputHandler.getValidatedInputWithExit(
                "Select an option (1 or 2): ",
                "Invalid choice. Please select 1 or 2, or type 'exit' to cancel.",
                input -> input.matches("[12]")
        );

        if (choice == null) {
            System.out.println("Update cancelled. Exiting...");
            return;
        }

        switch (choice) {
            case "1":
                MedicalRecordSystem.upsertBloodType(patientID);
                break;
            case "2":
                MedicalRecordSystem.upsertDiagnosis(patientID);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }


    /**
     * Sets the availability for appointments.
     */
    public void setAvailability() {
        String doctorID = getUserId();


        while (true) {
            System.out.println("\n--- Doctor Menu ---");
            System.out.println("1. View Availability");
            System.out.println("2. Add Availability");
            System.out.println("3. Remove Availability");
            System.out.println("4. Exit");

            String choice = InputHandler.getValidatedInput(
                    "Select an option: ",
                    "Invalid input. Please enter a number between 1 and 4.",
                    input -> input.matches("[1-4]")
            );

            switch (choice) {
                case "1":
                    AppointmentSystem.displayDoctorAvailability(doctorID);
                    break;
                case "2":
                    AppointmentSystem.addDoctorAvailabilityMenu(doctorID);
                    break;
                case "3":
                    AppointmentSystem.removeDoctorAvailability(doctorID);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
