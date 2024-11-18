package Users;

import Systems.AppointmentSystem;
import Models.MedicalRecord;
import Models.User;
import Systems.InputHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Represents a patient in the Hospital Management System.
 * This class handles the patient's personal and medical information.
 */
public class Patient extends User {

    private final MedicalRecord medicalRecord;

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
        this.medicalRecord = new MedicalRecord(userId);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> {
                System.out.println("Viewing medical record...");
                medicalRecord.displayMedicalRecord();
            }
            case 2 -> {
                System.out.println("Updating personal information...");
                updatePersonalInformation();
                System.out.println("Your updated personal information:");
                medicalRecord.displayMedicalRecord();
            }
            case 3 -> {
                System.out.println("Viewing available appointment slots...");
                AppointmentSystem.displayAvailableSlots();
            }
            case 4 -> {
                System.out.println("Scheduling an appointment...");
                scheduleAppointment();
            }
            case 5 -> {
                System.out.println("Rescheduling an appointment...");
                rescheduleAppointment();
            }
            case 6 -> {
                System.out.println("Cancelling an appointment...");
                cancelAppointment();
            }
            case 7 -> {
                System.out.println("Viewing scheduled appointments...");
                AppointmentSystem.displayAppointmentsByPatient(getUserId(), Collections.singletonList("confirmed"));
            }
            case 8 -> {
                System.out.println("Viewing past appointment outcomes...");
                AppointmentSystem.displayAppointmentsByPatient(getUserId(), Collections.singletonList("completed"));
            }
            case 9 -> {
                System.out.println("Logging out...");
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Updates the personal information of the patient interactively.
     */
    public void updatePersonalInformation() {
        System.out.println("Updating Personal Information...");
        System.out.print("Enter new Date of Birth (YYYY-MM-DD): ");
        String newDateOfBirth = InputHandler.nextLine();

        System.out.print("Enter new Phone Number: ");
        String newPhoneNumber = InputHandler.nextLine();

        System.out.print("Enter new Email Address: ");
        String newEmailAddress = InputHandler.nextLine();

        medicalRecord.setDateOfBirth(newDateOfBirth);
        medicalRecord.setPhoneNumber(newPhoneNumber);
        medicalRecord.setEmailAddress(newEmailAddress);

        System.out.println("Personal information updated successfully!");
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
