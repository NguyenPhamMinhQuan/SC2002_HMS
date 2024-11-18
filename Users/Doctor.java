package Users;

import Models.*;
import Systems.*;
import Systems.MedicalRecordSystem;

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

    /**
     * Records the outcome of an appointment.
     */
    private void recordAppointmentOutcome() {
        System.out.println("Recording appointment outcome...");

        // Get the list of confirmed appointments
        List<Appointment> confirmedAppointments = AppointmentSystem.getAppointmentsByDoctor(getUserId(), "confirmed");

        if (confirmedAppointments.isEmpty()) {
            System.out.println("No confirmed appointments available to record outcomes for.");
            return;
        }

        // Display confirmed appointments
        System.out.println("Select an appointment to record the outcome:");
        for (int i = 0; i < confirmedAppointments.size(); i++) {
            Appointment appointment = confirmedAppointments.get(i);
            System.out.printf("[%d] Appointment ID: %s, Patient ID: %s, Date: %s%n",
                    i + 1,
                    appointment.getID(),
                    appointment.getPatientID(),
                    AppointmentSystem.formatDate(appointment.getAppointmentDate()));
        }

        // Input selection
        System.out.print("Enter the number of the appointment: ");
        int choice = InputHandler.nextInt() - 1;

        if (choice < 0 || choice >= confirmedAppointments.size()) {
            System.out.println("Invalid choice. Returning to menu.");
            return;
        }

        Appointment selectedAppointment = confirmedAppointments.get(choice);

        // Record details for the appointment outcome
        System.out.print("Enter Service Type (e.g., Consultation, X-ray): ");
        String serviceType = InputHandler.nextLine();

        System.out.print("Enter Consultation Notes: ");
        String consultationNotes = InputHandler.nextLine();

        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                AppointmentSystem.formatDate(selectedAppointment.getAppointmentDate()),
                serviceType,
                consultationNotes
        );

        // Prescribe Medications
        StockSystem stockSystem = new StockSystem(); // Load the stock system
        System.out.println("Do you want to prescribe medications? (yes/no): ");
        String prescribeMedications = InputHandler.nextLine();

        if (prescribeMedications.equalsIgnoreCase("yes")) {
            while (true) {
                // Display available stocks
                System.out.println("Available medicines:");
                List<Stock> availableStocks = stockSystem.getStocks();
                for (Stock stock : availableStocks) {
                    System.out.printf("ID: %d, Name: %s, Stock Level: %d%n",
                            stock.getID(),
                            stock.getMedicineName(),
                            stock.getStockLevel());
                }

                System.out.print("Enter medicine ID (or type '0' to finish): ");
                int medicineID = InputHandler.nextInt();
                if (medicineID == 0) break;

                Stock selectedStock = stockSystem.getStockById(medicineID);
                if (selectedStock == null) {
                    System.out.println("Invalid medicine ID. Please try again.");
                    continue;
                }

                System.out.print("Enter quantity to prescribe: ");
                int quantity = InputHandler.nextInt();

                if (selectedStock.getStockLevel() < quantity) {
                    System.out.println("Insufficient stock for " + selectedStock.getMedicineName() + ". Available: " + selectedStock.getStockLevel());
                    continue;
                }

                // Deduct the prescribed quantity from the stock
                selectedStock.setStockLevel(selectedStock.getStockLevel() - quantity);
                stockSystem.saveStocks(); // Save updated stock levels

                // Add to the outcome record
                Stock prescribedMedication = new Stock(selectedStock.getMedicineName(), quantity);
                outcomeRecord.addMedication(prescribedMedication);

                System.out.println("Added " + quantity + " of " + selectedStock.getMedicineName() + " to the prescription.");
            }
        }

        // Save the outcome to the system
        MedicalRecord patientRecord = MedicalRecordSystem.loadMedicalRecord(String.format("%s",selectedAppointment.getPatientID()));
        if (patientRecord != null) {
            patientRecord.addAppointmentOutcome(outcomeRecord);
            MedicalRecordSystem.saveMedicalRecord(patientRecord);
            System.out.println("Appointment outcome recorded successfully.");
        } else {
            System.out.println("Failed to record outcome. Patient's medical record not found.");
            return;
        }

        // Update the appointment status to "complete"
        AppointmentSystem.updateAppointmentStatus(selectedAppointment.getID(), "completed");
        System.out.println("Appointment status updated to 'complete'.");
    }
}
