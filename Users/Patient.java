package Users;

import Enums.AppointmentStatus;
import Enums.UserRole;
import Models.User;
import Systems.AppointmentOutcomeSystem;
import Systems.AppointmentSystem;
import Systems.InputHandler;
import Systems.MedicalRecordSystem;

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
     * Allows the patient to schedule an appointment.
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
