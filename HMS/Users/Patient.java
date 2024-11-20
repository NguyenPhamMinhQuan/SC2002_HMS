package HMS.Users;

import HMS.Enums.AppointmentStatus;
import HMS.Enums.UserRole;
import HMS.Models.User;
import HMS.Systems.AppointmentOutcomeSystem;
import HMS.Systems.AppointmentSystem;
import HMS.Systems.InputHandler;
import HMS.Systems.MedicalRecordSystem;

import java.util.Arrays;
import java.util.Date;

/**
 * Represents a patient in the Hospital Management System.
 * This class handles the patient's personal and medical information.
 */
public class Patient extends User implements UserMenuInterface {
    /**
     * Constructs a new Patient with the specified details and an empty medical record.
     *
     * @param userId   the unique ID of the patient.
     * @param password the password of the patient.
     * @param name     the name of the patient.
     * @param gender   the gender of the patient (Male or Female).
     * @param age      the age of the patient.
     */
    public Patient(String userId, String password, String name, String gender, int age) {
        super(userId, password, UserRole.PATIENT, name, gender, age);
    }

    /**
     * Executes the specified feature from the patient's menu.
     *
     * @param feature the feature option to execute.
     *                <ul>
     *                  <li>1 - View medical record</li>
     *                  <li>2 - Update Personal Information: </li>
     *                  <li>3 - View Available Appointment Slots</li>
     *                  <li>4 - Schedule an Appointment</li>
     *                  <li>5 - Reschedule an Appointment</li>
     *                  <li>6 - Cancel an Appointment</li>
     *                  <li>7 - View Scheduled Appointments</li>
     *                  <li>8 - View Past Appointment Outcome Records</li>
     *                  <li>9 - Logout</li>
     *                </ul>
     * @return {@code true} if patient chooses to exit the menu; {@code false} otherwise.
     */
    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> MedicalRecordSystem.showOrCreateMedicalRecord(getUserId());
            case 2 -> MedicalRecordSystem.upsertMedicalRecord(getUserId());
            case 3 -> AppointmentSystem.displayAllDoctorsAvailability();
            case 4 -> scheduleAppointment();
            case 5 -> AppointmentSystem.rescheduleAppointment(getUserId());
            case 6 -> cancelAppointment();
            case 7 -> AppointmentSystem.displayAppointmentsByPatient(getUserId(), Arrays.asList(AppointmentStatus.PENDING, AppointmentStatus.APPROVED));
            case 8 -> AppointmentOutcomeSystem.displayOutcomesForPatient(getUserId());
            case 9 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Allows the patient to schedule an appointment by selecting a doctor and an available timeslot
     * If no slots are available, the scheduling process is cancelled
     */
    private void scheduleAppointment() {
        String doctorID = AppointmentSystem.selectDoctorWithAvailableSlots();
        if (doctorID == null) {
            System.out.println("No doctors with available slots or selection canceled.");
            return;
        }

        Date selectedSlot = AppointmentSystem.selectSlotForDoctor(doctorID);
        if (selectedSlot == null) {
            System.out.println("No slots available for this doctor or selection canceled.");
            return;
        }

        AppointmentSystem.scheduleAppointment(getUserId(), doctorID, selectedSlot);
    }

    /**
     * Allows the patient to cancel an appointment.
     */
    private void cancelAppointment() {
        String patientID = getUserId();

        System.out.println("--- Your Appointments ---");
        AppointmentSystem.displayAppointmentsByPatient(patientID, Arrays.asList(AppointmentStatus.PENDING, AppointmentStatus.APPROVED));

        String input = InputHandler.getValidatedInputWithExit(
                "Enter the Appointment ID to cancel: ",
                "Invalid input. Please enter a valid appointment ID.",
                value -> {
                    try {
                        int id = Integer.parseInt(value);
                        return AppointmentSystem.getAppointmentsByPatient(patientID, null)
                                .stream()
                                .anyMatch(appointment -> appointment.getID() == id && !(appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED));
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        if (input != null) {
            int appointmentID = Integer.parseInt(input);
            AppointmentSystem.cancelAppointment(patientID, appointmentID);
        } else {
            System.out.println("Cancellation process aborted.");
        }
    }
}
