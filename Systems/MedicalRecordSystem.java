package Systems;

import Models.Diagnosis;
import Models.MedicalRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * System to manage medical records, including saving, updating, and loading records.
 */
public class MedicalRecordSystem {

    private static final String MEDICAL_RECORD_CSV = "data/medical_records.csv";

    /**
     * Ensures the medical records file exists and creates it with a header if it doesn't.
     */
    private static void ensureFileExistsWithHeader() {
        File file = new File(MEDICAL_RECORD_CSV);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType,Diagnoses");
                bw.newLine();
                System.out.println("Medical records file created with headers: " + MEDICAL_RECORD_CSV);
            } catch (IOException e) {
                System.err.println("Error creating medical records file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves or updates a medical record in the CSV file.
     *
     * @param medicalRecord the medical record to save or update.
     */
    public static void saveMedicalRecord(MedicalRecord medicalRecord) {
        ensureFileExistsWithHeader();

        File file = new File(MEDICAL_RECORD_CSV);
        List<String> allRecords = new ArrayList<>();
        boolean recordUpdated = false;

        try {
            // Read all existing records
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String header = reader.readLine(); // Read the header
                if (header != null) {
                    allRecords.add(header); // Keep the header intact
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length > 0 && fields[0].equals(medicalRecord.getPatientID())) {
                        // Update the existing record
                        allRecords.add(serializeMedicalRecord(medicalRecord));
                        recordUpdated = true;
                    } else {
                        // Keep other records unchanged
                        allRecords.add(line);
                    }
                }
            }

            // If the record was not found, add it as a new record
            if (!recordUpdated) {
                allRecords.add(serializeMedicalRecord(medicalRecord));
            }

            // Write all records back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String record : allRecords) {
                    writer.write(record);
                    writer.newLine();
                }
            }

            System.out.println("Medical record saved or updated successfully.");
        } catch (IOException e) {
            System.err.println("Error saving medical record: " + e.getMessage());
        }
    }

    /**
     * Serializes a list of diagnoses into a CSV-compatible string.
     *
     * @param diagnoses the list of diagnoses to serialize.
     * @return a string representing the serialized diagnoses.
     */
    private static String serializeDiagnoses(List<Diagnosis> diagnoses) {
        StringBuilder serialized = new StringBuilder();
        for (Diagnosis diagnosis : diagnoses) {
            serialized.append(diagnosis.getCondition()).append("|")
                    .append(diagnosis.getDiagnosisDate()).append("|")
                    .append(diagnosis.getPrescription()).append("|")
                    .append(diagnosis.getPrescriptionStatus()).append(";");
        }
        return serialized.toString();
    }

    /**
     * Serializes a medical record into a CSV-compatible string.
     *
     * @param medicalRecord the medical record to serialize.
     * @return a string representing the serialized medical record.
     */
    private static String serializeMedicalRecord(MedicalRecord medicalRecord) {
        return String.join(",",
                medicalRecord.getPatientID(),
                medicalRecord.getDateOfBirth(),
                medicalRecord.getPhoneNumber(),
                medicalRecord.getEmailAddress(),
                medicalRecord.getBloodType(),
                serializeDiagnoses(medicalRecord.getDiagnoses())
        );
    }

    /**
     * Deserializes a string of diagnoses into a list of Diagnosis objects.
     *
     * @param diagnosesData the serialized diagnoses data.
     * @return a list of Diagnosis objects.
     */
    private static List<Diagnosis> deserializeDiagnoses(String diagnosesData) {
        List<Diagnosis> diagnoses = new ArrayList<>();
        if (diagnosesData == null || diagnosesData.equalsIgnoreCase("None") || diagnosesData.isEmpty()) {
            return diagnoses; // Return empty list if no diagnoses
        }

        String[] diagnosisEntries = diagnosesData.split(";");
        for (String entry : diagnosisEntries) {
            String[] parts = entry.split("\\|");
            if (parts.length == 4) {
                diagnoses.add(new Diagnosis(parts[0], parts[1], parts[2], parts[3]));
            } else {
                System.err.println("Invalid diagnosis entry: " + entry);
            }
        }
        return diagnoses;
    }

    /**
     * Loads a medical record for a given patient ID.
     *
     * @param patientID the ID of the patient whose record needs to be loaded.
     * @return the medical record if found; null otherwise.
     */
    public static MedicalRecord loadMedicalRecord(String patientID) {
        ensureFileExistsWithHeader();

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICAL_RECORD_CSV))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (line.startsWith("PatientID")) continue;

                // Split on commas but handle embedded commas and missing fields
                String[] recordDetails = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (recordDetails.length < 5) { // Ensure all required fields are present
                    System.err.println("Invalid record: " + line);
                    continue;
                }

                // Match the patient ID
                if (recordDetails[0].equalsIgnoreCase(patientID)) {
                    MedicalRecord medicalRecord = new MedicalRecord(patientID);
                    medicalRecord.setDateOfBirth(recordDetails[1]);
                    medicalRecord.setPhoneNumber(recordDetails[2]);
                    medicalRecord.setEmailAddress(recordDetails[3]);
                    medicalRecord.setBloodType(recordDetails[4]);

                    // Parse diagnoses if present
                    if (recordDetails.length > 5) {
                        List<Diagnosis> diagnoses = deserializeDiagnoses(recordDetails[5]);
                        medicalRecord.addDiagnoses(diagnoses);
                    }

                    return medicalRecord; // Return the loaded record
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Return null if the record does not exist
        return null;
    }

    public static void showOrCreateMedicalRecord(String userID) {
        ensureFileExistsWithHeader();

        // Load the medical record for the given user ID
        MedicalRecord medicalRecord = loadMedicalRecord(userID);

        if (medicalRecord == null) {
            System.out.println("Medical record not found for User ID: " + userID);
            System.out.println("Prompting user to create a new medical record...");
            // Reuse updateOrAddMedicalRecord to handle creation
            updateOrAddMedicalRecord(userID);

            // Load the newly created medical record
            medicalRecord = loadMedicalRecord(userID);
        }

        // Display the medical record
        System.out.println("\n--- Medical Record for User ID: " + userID + " ---");

        // Display personal details in a table format
        System.out.println("+--------------------+--------------------------+");
        System.out.printf("| %-18s | %-24s |\n", "Date of Birth", medicalRecord.getDateOfBirth());
        System.out.printf("| %-18s | %-24s |\n", "Phone Number", medicalRecord.getPhoneNumber());
        System.out.printf("| %-18s | %-24s |\n", "Email Address", medicalRecord.getEmailAddress());
        System.out.printf("| %-18s | %-24s |\n", "Blood Type", medicalRecord.getBloodType());
        System.out.println("+--------------------+--------------------------+");

        // Display diagnoses
        System.out.println("Diagnoses:");
        if (medicalRecord.getDiagnoses().isEmpty()) {
            System.out.println("No diagnoses recorded.");
        } else {
            System.out.println("+--------------------+---------------+-------------------------+--------------------+");
            System.out.printf("| %-18s | %-13s | %-23s | %-18s |\n", "Condition", "Date", "Prescription", "Status");
            System.out.println("+--------------------+---------------+-------------------------+--------------------+");
            for (Diagnosis diagnosis : medicalRecord.getDiagnoses()) {
                System.out.printf("| %-18s | %-13s | %-23s | %-18s |\n",
                        diagnosis.getCondition(),
                        diagnosis.getDiagnosisDate(),
                        diagnosis.getPrescription(),
                        diagnosis.getPrescriptionStatus());
            }
            System.out.println("+--------------------+---------------+-------------------------+--------------------+");
        }
    }

    public static void updateOrAddMedicalRecord(String userID) {
        ensureFileExistsWithHeader();

        // Load the medical record for the given user ID or create a new one if it doesn't exist
        MedicalRecord medicalRecord = loadMedicalRecord(userID);
        boolean isNewRecord = (medicalRecord == null);

        if (isNewRecord) {
            System.out.println("Medical record not found for User ID: " + userID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(userID);
        } else {
            System.out.println("\n--- Updating Medical Record for User ID: " + userID + " ---");
            System.out.println("Leave the input blank to retain the current value.");
        }

        // Prompt for Date of Birth
        String currentDateOfBirth = medicalRecord.getDateOfBirth();
        if (currentDateOfBirth != null && !currentDateOfBirth.isEmpty()) {
            System.out.println("Current Date of Birth: " + currentDateOfBirth);
        }
        String newDateOfBirth = InputHandler.getValidatedInput(
                "Enter new Date of Birth (YYYY-MM-DD): ",
                "Invalid date format. Please use YYYY-MM-DD.",
                input -> input.isEmpty() || input.matches("\\d{4}-\\d{2}-\\d{2}")
        );
        if (!newDateOfBirth.trim().isEmpty()) {
            medicalRecord.setDateOfBirth(newDateOfBirth);
        }

        // Prompt for Phone Number
        String currentPhoneNumber = medicalRecord.getPhoneNumber();
        if (currentPhoneNumber != null && !currentPhoneNumber.isEmpty()) {
            System.out.println("Current Phone Number: " + currentPhoneNumber);
        }
        String newPhoneNumber = InputHandler.getValidatedInput(
                "Enter new Phone Number: ",
                "Invalid phone number. Must contain 8-10 digits.",
                input -> input.isEmpty() || input.matches("\\d{8,10}")
        );
        if (!newPhoneNumber.trim().isEmpty()) {
            medicalRecord.setPhoneNumber(newPhoneNumber);
        }

        // Prompt for Email Address
        String currentEmailAddress = medicalRecord.getEmailAddress();
        if (currentEmailAddress != null && !currentEmailAddress.isEmpty()) {
            System.out.println("Current Email Address: " + currentEmailAddress);
        }
        String newEmailAddress = InputHandler.getValidatedInput(
                "Enter new Email Address: ",
                "Invalid email address format.",
                input -> input.isEmpty() || input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        );
        if (!newEmailAddress.trim().isEmpty()) {
            medicalRecord.setEmailAddress(newEmailAddress);
        }

        // Prompt for Blood Type
        String currentBloodType = medicalRecord.getBloodType();
        if (currentBloodType != null && !currentBloodType.isEmpty()) {
            System.out.println("Current Blood Type: " + currentBloodType);
        }
        String newBloodType = InputHandler.getValidatedInput(
                "Enter new Blood Type (e.g., A+, B-, O+): ",
                "Invalid blood type. Must be A+, A-, B+, B-, AB+, AB-, O+, O-.",
                input -> input.isEmpty() || input.matches("^(A|B|AB|O)[+-]$")
        );
        if (!newBloodType.trim().isEmpty()) {
            medicalRecord.setBloodType(newBloodType);
        }

        // Save the updated or newly created record
        saveMedicalRecord(medicalRecord);

        if (isNewRecord) {
            System.out.println("New medical record created successfully for User ID: " + userID);
        } else {
            System.out.println("Medical record updated successfully for User ID: " + userID);
        }
    }

    /**
     * Updates the blood type of a medical record and saves the changes to the CSV file.
     * If the medical record does not exist, it creates a new one.
     *
     * @param userID the ID of the user whose blood type needs to be updated.
     */
    public static void updateBloodType(String userID) {
        ensureFileExistsWithHeader();

        // Load the medical record or create a new one if it doesn't exist
        MedicalRecord medicalRecord = loadMedicalRecord(userID);
        if (medicalRecord == null) {
            System.out.println("Medical record not found for User ID: " + userID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(userID);
        }

        // Prompt the user for the new blood type
        String newBloodType = InputHandler.getValidatedInput(
                "Enter new Blood Type (e.g., A+, B-, O+): ",
                "Invalid blood type. Must be A+, A-, B+, B-, AB+, AB-, O+, O-.",
                input -> input.matches("^(A|B|AB|O)[+-]$")
        );

        try {
            // Update the blood type
            medicalRecord.setBloodType(newBloodType);

            // Save the updated record to the CSV
            saveMedicalRecord(medicalRecord);

            System.out.println("Blood type updated successfully for User ID: " + userID);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Updates or adds a diagnosis in the medical record and saves the changes to the CSV file.
     * If the medical record does not exist, it creates a new one.
     *
     * @param userID the ID of the user whose diagnosis needs to be updated.
     */
    public static void updateDiagnosis(String userID) {
        ensureFileExistsWithHeader();

        // Load the medical record or create a new one if it doesn't exist
        MedicalRecord medicalRecord = loadMedicalRecord(userID);
        if (medicalRecord == null) {
            System.out.println("Medical record not found for User ID: " + userID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(userID);
        }

        // Prompt the user for diagnosis details
        String condition = InputHandler.getValidatedInput(
                "Enter Diagnosis Condition: ",
                "Condition cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        String diagnosisDate = InputHandler.getValidatedInput(
                "Enter Diagnosis Date (YYYY-MM-DD): ",
                "Invalid date format. Please use YYYY-MM-DD.",
                input -> input.matches("\\d{4}-\\d{2}-\\d{2}")
        );

        String prescription = InputHandler.getValidatedInput(
                "Enter Prescription: ",
                "Prescription cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        String prescriptionStatus = InputHandler.getValidatedInput(
                "Enter Prescription Status (e.g., Completed, Ongoing): ",
                "Status cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        // Create the new diagnosis
        Diagnosis newDiagnosis = new Diagnosis(condition, diagnosisDate, prescription, prescriptionStatus);

        // Ensure the diagnoses list is mutable
        List<Diagnosis> diagnoses = new ArrayList<>(medicalRecord.getDiagnoses());

        // Update or add the diagnosis
        boolean updated = false;
        for (int i = 0; i < diagnoses.size(); i++) {
            if (diagnoses.get(i).getCondition().equalsIgnoreCase(newDiagnosis.getCondition())) {
                diagnoses.set(i, newDiagnosis);
                updated = true;
                break;
            }
        }

        if (!updated) {
            diagnoses.add(newDiagnosis);
        }

        // Update the medical record's diagnoses
        medicalRecord.addDiagnoses(diagnoses);

        // Save the updated record to the CSV
        saveMedicalRecord(medicalRecord);
        System.out.println("Diagnosis " + (updated ? "updated" : "added") + " successfully for User ID: " + userID);
    }
}