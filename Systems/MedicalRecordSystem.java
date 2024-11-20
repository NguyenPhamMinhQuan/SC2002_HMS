package Systems;

import Models.MedicalRecord;
import Repositories.MedicalRecordRepository;
import Models.Diagnosis;

import java.util.List;

public class MedicalRecordSystem {

    private static MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();

    public static void showOrCreateMedicalRecord(String userID) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientID(userID);

        if (medicalRecord == null) {
            System.out.println("Medical record not found for User ID: " + userID);
            System.out.println("Prompting user to create a new medical record...");

            upsertMedicalRecord(userID); // This is where the error occurred

            medicalRecord = medicalRecordRepository.findByPatientID(userID);
        }

        assert medicalRecord != null; // To fix warnings, should not happen

        System.out.println("\n--- Medical Record for User ID: " + userID + " ---");
        System.out.println("+--------------------+--------------------------+");
        System.out.printf("| %-18s | %-24s |\n", "Date of Birth", medicalRecord.getDateOfBirth());
        System.out.printf("| %-18s | %-24s |\n", "Phone Number", medicalRecord.getPhoneNumber());
        System.out.printf("| %-18s | %-24s |\n", "Email Address", medicalRecord.getEmailAddress());
        System.out.printf("| %-18s | %-24s |\n", "Blood Type", medicalRecord.getBloodType());
        System.out.println("+--------------------+--------------------------+");

        System.out.println("Diagnoses:");
        if (medicalRecord.getDiagnoses().isEmpty()) {
            System.out.println("No diagnoses recorded.");
        } else {
            System.out.println("+--------------------+---------------+-------------------------+");
            System.out.printf("| %-18s | %-13s | %-23s |\n", "Condition", "Date", "Prescription");
            System.out.println("+--------------------+---------------+-------------------------+");
            for (Diagnosis diagnosis : medicalRecord.getDiagnoses()) {
                System.out.printf("| %-18s | %-13s | %-23s |\n",
                        diagnosis.getCondition(),
                        diagnosis.getDiagnosisDate(),
                        diagnosis.getPrescription()
                );
            }
            System.out.println("+--------------------+---------------+-------------------------+");
        }
    }

    // Implement upsertMedicalRecord
    public static void upsertMedicalRecord(String userID) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientID(userID);

        if (medicalRecord == null) {
            // If no medical record exists, create a new one
            System.out.println("Creating new medical record for User ID: " + userID);
            medicalRecord = new MedicalRecord(userID);
        } else {
            // If a medical record exists, update it
            System.out.println("\n--- Updating Medical Record for User ID: " + userID + " ---");
        }

        // Prompt for and update the medical record fields
        String newDateOfBirth = InputHandler.getValidatedInput(
                "Enter new Date of Birth (YYYY-MM-DD): ",
                "Invalid date format. Please use YYYY-MM-DD.",
                input -> input.isEmpty() || input.matches("\\d{4}-\\d{2}-\\d{2}")
        );
        if (!newDateOfBirth.trim().isEmpty()) {
            medicalRecord.setDateOfBirth(newDateOfBirth);
        }

        String newPhoneNumber = InputHandler.getValidatedInput(
                "Enter new Phone Number: ",
                "Invalid phone number. Must contain 8-10 digits.",
                input -> input.isEmpty() || input.matches("\\d{8,10}")
        );
        if (!newPhoneNumber.trim().isEmpty()) {
            medicalRecord.setPhoneNumber(newPhoneNumber);
        }

        String newEmailAddress = InputHandler.getValidatedInput(
                "Enter new Email Address: ",
                "Invalid email address format.",
                input -> input.isEmpty() || input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        );
        if (!newEmailAddress.trim().isEmpty()) {
            medicalRecord.setEmailAddress(newEmailAddress);
        }

        String newBloodType = InputHandler.getValidatedInput(
                "Enter new Blood Type (e.g., A+, B-, O+): ",
                "Invalid blood type. Must be A+, A-, B+, B-, AB+, AB-, O+, O-.",
                input -> input.isEmpty() || input.matches("^(A|B|AB|O)[+-]$")
        );
        if (!newBloodType.trim().isEmpty()) {
            medicalRecord.setBloodType(newBloodType);
        }

        // Save the updated medical record
        medicalRecordRepository.saveData(List.of(medicalRecord));

        System.out.println("Medical record " + (medicalRecord.getDateOfBirth() == null ? "created" : "updated") + " successfully for User ID: " + userID);
    }

    public static void saveMedicalRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.loadData();
        medicalRecords.add(medicalRecord);
        medicalRecordRepository.saveData(medicalRecords);
    }
}
