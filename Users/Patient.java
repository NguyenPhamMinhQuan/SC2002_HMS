package Users;

import Models.MedicalRecord;
import Models.User;
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
        super(userId, password, "patient", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> MedicalRecordSystem.showOrCreateMedicalRecord(getUserId());
            case 2 -> MedicalRecordSystem.updateOrAddMedicalRecord(getUserId());
            case 3 -> AppointmentSystem.displayAvailableSlots();
            case 4 -> scheduleAppointment();
            case 5 -> rescheduleAppointment();
            case 6 -> cancelAppointment();
            case 7 -> AppointmentSystem.displayAppointmentsByPatient(getUserId(), Arrays.asList("confirmed", "pending"));
            case 8 -> {
                System.out.println("Viewing past appointment outcomes...");
            }
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
        AppointmentSystem.displayAvailableSlots();
        System.out.print("Enter Doctor ID: ");
        String doctorID = InputHandler.nextLine();
        System.out.print("Enter Appointment Date and Time (YYYY-MM-DD HH:mm): ");
        Date appointmentDate = AppointmentSystem.parseDate(InputHandler.nextLine());

        if (appointmentDate != null) {
            AppointmentSystem.scheduleAppointment(getUserId(), doctorID, appointmentDate);
        } else {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    /**
     * Allows the patient to reschedule an appointment.
     */
    private void rescheduleAppointment() {
        System.out.println("Your Scheduled Appointments:");
        AppointmentSystem.displayAppointmentsByPatient(getUserId(), Arrays.asList("confirmed", "pending"));

        System.out.print("Enter Appointment ID to reschedule: ");
        int appointmentID = InputHandler.nextInt();
        AppointmentSystem.displayAvailableSlots();
        System.out.print("Enter new Appointment Date and Time (YYYY-MM-DD HH:mm): ");
        Date newDate = AppointmentSystem.parseDate(InputHandler.nextLine());

        if (newDate != null) {
            AppointmentSystem.updateAppointment(appointmentID, newDate);
        } else {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    /**
     * Allows the patient to cancel an appointment.
     */
    private void cancelAppointment() {
        System.out.println("Your Scheduled Appointments:");
        AppointmentSystem.displayAppointmentsByPatient(getUserId(), Arrays.asList("confirmed", "pending"));

        System.out.print("Enter Appointment ID to cancel: ");
        int appointmentID = InputHandler.nextInt();
        AppointmentSystem.cancelAppointment(appointmentID);
    }
}
