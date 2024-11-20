package HMS.Users;

import HMS.Enums.UserRole;
import HMS.Models.Appointment;
import HMS.Models.User;
import HMS.Systems.*;

import java.util.List;
import java.util.stream.Collectors;


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

    /**
     * Processes the selected feature from the doctor's menu.
     *
     * @param feature the feature option to execute.
     *                <ul>
     *                  <li>0 - Change password</li>
     *                  <li>1 - View medical record</li>
     *                  <li>2 - Update medical record</li>
     *                  <li>3 - View Personal Schedule</li>
     *                  <li>4 - Manage Availability for Appointments</li>
     *                  <li>5 - Accept or Decline Appointment Requests</li>
     *                  <li>6 - View Upcoming Appointments</li>
     *                  <li>7 - Record Appointment Outcome</li>
     *                  <li>8 - Logout</li>
     *                </ul>
     * @return {@code true} if the doctor chooses to exit the menu, otherwise {@code false}.
     */
    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 0 -> UserManagementSystem.updatePassword(getUserId());
            case 1 -> {
                // Retrieve all appointments for the current doctor
                List<Appointment> doctorAppts = AppointmentSystem.getAppointments().stream()
                        .filter(x -> x.getDoctorID().equals(getUserId()))
                        .collect(Collectors.toList());

                // Retrieve all patients for the current doctor based on their appointments
                List<User> doctorPatients = UserManagementSystem.getUsersByRole(UserRole.PATIENT).stream()
                        .filter(patient -> doctorAppts.stream()
                                .anyMatch(appointment -> appointment.getPatientID().equals(patient.getUserId())))
                        .collect(Collectors.toList());

                // Show or create the medical record for the selected patient
                String selectedPatientId = UserManagementSystem.selectUserIDMenu(doctorPatients);
                MedicalRecordSystem.showOrCreateMedicalRecord(selectedPatientId);
            }
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
     * The user can select a patient an update the patient's blood type or add a diagnosis.
     */
    public void updateMedicalRecord() {
        // Retrieve all appointments for the current doctor
        List<Appointment> doctorAppts = AppointmentSystem.getAppointments().stream()
                .filter(x -> x.getDoctorID().equals(getUserId()))
                .collect(Collectors.toList());

        // Retrieve all patients for the current doctor based on their appointments
        List<User> doctorPatients = UserManagementSystem.getUsersByRole(UserRole.PATIENT).stream()
                .filter(patient -> doctorAppts.stream()
                        .anyMatch(appointment -> appointment.getPatientID().equals(patient.getUserId())))
                .collect(Collectors.toList());

        // Show or create the medical record for the selected patient
        String patientID = UserManagementSystem.selectUserIDMenu(doctorPatients);

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
     * Sets the doctor's availability for appointments.
     * The doctor can view, add, or remove availability, or exit the menu.
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
