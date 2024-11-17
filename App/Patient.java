import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

/**
 * Represents a patient in the Hospital Management System.
 * This class handles the patient's personal and medical information.
 */
public class Patient extends User {

    private MedicalRecord medicalRecord;

    /**
     * Constructs a new Patient with the specified details and an empty medical records
     * 
     * @param userId       the unique ID of the patient.
     * @param password     the password of the patient.
     * @param name         the name of the patient.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user 
     */
    public Patient(String userId, String password, String name, String gender, int age) {
        super(userId, password, "patient", name, gender, age);  // Patient's role is "patient"
        this.medicalRecord = new MedicalRecord(userId);
        updatePersonalInformation();
    }

    @Override
    public boolean functionCall(int feature) {
        Scanner scanner = new Scanner(System.in);
        switch (feature) {
            case 1 -> {
                System.out.println("Viewing medical record...");
                medicalRecord.displayMedicalRecord();
            }
            case 2 -> {
                System.out.println("Updating personal information...");
                updatePersonalInformation();
                System.out.println("Your medical record is updated below:");
                medicalRecord.displayMedicalRecord();
            }
            case 3 -> {
                System.out.println("Viewing available appointment slots...");
                // todo
            }
            case 4 -> {
                System.out.println("Scheduling an appointment...");
                System.out.print("Enter Doctor ID: ");
                String doctorID = scanner.nextLine();
                System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
                Date appointmentDate = AppointmentSystem.parseDate(scanner.nextLine());
                AppointmentSystem.scheduleAppointment(getUserId(), doctorID, appointmentDate);
            }
            case 5 -> {
                System.out.println("Rescheduling an appointment...");
                System.out.print("Enter Appointment ID to Reschedule: ");
                int appointmentID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter New Appointment Date (YYYY-MM-DD): ");
                Date newDate = AppointmentSystem.parseDate(scanner.nextLine());
                AppointmentSystem.updateAppointment(appointmentID, newDate);
            }
            case 6 -> {
                System.out.println("Cancelling an appointment...");
                System.out.print("Enter Appointment ID to Cancel: ");
                int appointmentID = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                AppointmentSystem.cancelAppointment(appointmentID);
            }
            case 7 -> {
                System.out.println("Viewing scheduled appointments...");
                AppointmentSystem.displayAppointmentsByPatient(getUserId(), "pending");
            }
            case 8 -> {
                System.out.println("Viewing past appointment outcomes...");
                AppointmentSystem.displayAppointmentsByPatient(getUserId(), "completed");
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
     * Allow patients to view their medical record
     * 
     */
    public void viewMedicalRecord() {
        medicalRecord.displayMedicalRecord();
    }

    public void updatePersonalInformation() {
        /**
         * Updates the personal information of the patient interactively by prompting the user for inputs.
         * This method allows updating the date of birth, phone number, and email address of the patient.
         * 
         * User inputs are collected via a Scanner instance.
         * 
         * Precondition: This method should only be called for a patient with an initialized medical record.
         * 
         * Postcondition: The patient's medical record is updated with the new personal information.
         * 
         * Example Usage:
         * <pre>
         * patient.updatePersonalInformationInteractive();
         * </pre>
         */
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Updating Personal Information");
    
        // Prompting the user for each attribute
        System.out.print("Enter new Date of Birth (YYYY-MM-DD): ");
        String newDateOfBirth = scanner.nextLine();
    
        System.out.print("Enter new Phone Number: ");
        String newPhoneNumber = scanner.nextLine();
    
        System.out.print("Enter new Email Address: ");
        String newEmailAddress = scanner.nextLine();
    
        // Update the medical record with the provided inputs
        this.medicalRecord.setDateOfBirth(newDateOfBirth);
        this.medicalRecord.setPhoneNumber(newPhoneNumber);
        this.medicalRecord.setEmailAddress(newEmailAddress);
    
        System.out.println("Personal information updated successfully!");
        scanner.close();
    }

}
