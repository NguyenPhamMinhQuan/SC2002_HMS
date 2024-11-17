import java.util.Scanner;
import java.util.Date;
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
    public Doctor( String userId, String password, String name, String gender, int age) {
        super( userId, password, "Doctor", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> {
                System.out.println("Viewing patient medical records...");
                String patientID = choosePatient();
//                viewMedicalRecord(medicalRecord);

            }
            case 2 -> {
                System.out.println("Updating patient medical records...");
                String patientID = choosePatient();
//                updateBloodType(medicalRecord);
            }
            case 3 -> {
                System.out.println("Viewing personal schedule...");
                // Shows all appointments regardless of status
                AppointmentSystem.displayAppointmentsByDoctor(getUserId(), null);
            }
            case 4 -> {
                System.out.println("Setting availability for appointments...");
//                AppointmentSystem.setAvailability(getUserId());
            }
            case 5 -> {
                System.out.println("Accepting or declining appointment requests...");
//                AppointmentSystem.cancelAppointment(getUserId());
            }
            case 6 -> {
                System.out.println("Viewing upcoming appointments...");
                AppointmentSystem.displayAppointmentsByDoctor(getUserId(), "pending");

            }
            case 7 -> {
                System.out.println("Recording appointment outcome...");
//                appointmentOutcome = new AppointmentOutcome();
//                AppointmentSystem.addAppointmentOutcome(getUserId());
            }
            case 8 -> {System.out.println("Logging out..."); return true;}
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose among the available Patients:");

        // Filter and display patients only
        UserManagementSystem.filterPatients();

        System.out.print("PatientID chosen: ");
        String patientID = scanner.next();
        scanner.close();

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
    public void viewMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecord.displayMedicalRecord();
    }

    /**
     * Updates the blood type in a patient's medical record.
     * 
     * @param medicalRecord the medical record to update.
     * @param newBloodType  the new blood type to set.
     */
    public void updateBloodType(MedicalRecord medicalRecord) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter patient's updated BloodType: ");
        String newBloodType = scanner.next();
        medicalRecord.setBloodType(newBloodType);
        System.out.println("Blood type updated to " + newBloodType + " for patient ID: " + medicalRecord.getPatientID());

        System.out.println("Please review the updated changes: ");
        medicalRecord.displayMedicalRecord();

        scanner.close();
    }

    /**
     * Adds a new diagnosis to a patient's medical record.
     * 
     * @param medicalRecord      the medical record to update.
     * @param diagnosisCondition the condition diagnosed.
     * @param diagnosisDate      the date of the diagnosis.
     * @param treatmentPlan      the treatment plan for the condition.
     * @param prescriptionStatus the status of the prescription (e.g., Approved, Pending).
     */
    public void addDiagnosis(MedicalRecord medicalRecord, String diagnosisCondition, Date diagnosisDate, String treatmentPlan, String prescriptionRequest, String prescriptionStatus) {
//        Diagnosis diagnosis = new Diagnosis(diagnosisCondition, diagnosisDate, treatmentPlan, prescriptionRequest, prescriptionStatus);
//        medicalRecord.addDiagnosis(diagnosis);
//        System.out.println("Added diagnosis: " + diagnosisCondition + " for patient ID: " + medicalRecord.getPatientID());
    }
}
