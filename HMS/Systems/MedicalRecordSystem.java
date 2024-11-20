package HMS.Systems;

import HMS.Models.Diagnosis;
import HMS.Models.MedicalRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * System to manage medical records, including saving, updating, and loading records.
 */
public class MedicalRecordSystem {

    private static final String MEDICAL_RECORDS_FILE = "data/medical_records.csv";

    // Display / Modifiers --

    /**
     * Displays the medical record for the given patientID, or prompts the patient
     * to create a new record if no record exists
     *
     * @param patientID ID of the patient whose medical record is to be displayed.
     */
    public static void showOrCreateMedicalRecord(String patientID) {
        ensureFileExistsWithHeader();

        if(patientID == null){
            return;
        }

        // Load the medical record for the given patient ID
        MedicalRecord medicalRecord = loadMedicalRecord(patientID);

        if (medicalRecord == null) {
            System.out.println("Medical record not found for patient ID: " + patientID);
            System.out.println("Prompting patient to create a new medical record...");

            upsertMedicalRecord(patientID);

            medicalRecord = loadMedicalRecord(patientID);
        }

        assert medicalRecord != null; // To fix warnings, should not happen

        System.out.println("\n--- Medical Record for patient ID: " + patientID + " ---");
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

    /**
     * Creates or updates a medical record for the given patient ID.
     * If no record exists, a new one is created.
     * Prompts the patient for new values, which can be left blank to retain the current value.
     *
     * @param patientID the ID of the patient whose medical record needs to be created or updated.
     */
    public static void upsertMedicalRecord(String patientID) {
        ensureFileExistsWithHeader();

        MedicalRecord medicalRecord = loadMedicalRecord(patientID);
        boolean isNewRecord = (medicalRecord == null);

        if (isNewRecord) {
            System.out.println("Medical record not found for patient ID: " + patientID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(patientID);
        } else {
            System.out.println("\n--- Updating Medical Record for patient ID: " + patientID + " ---");
            System.out.println("Leave the input blank to retain the current value.");
        }

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

        saveMedicalRecord(medicalRecord);

        if (isNewRecord) {
            System.out.println("New medical record created successfully for patient ID: " + patientID);
        } else {
            System.out.println("Medical record updated successfully for patient ID: " + patientID);
        }
    }

    /**
     * Updates the blood type of medical record and saves the changes to the CSV file.
     * If the medical record does not exist, it creates a new one.
     *
     * @param patientID the ID of the patient whose blood type needs to be updated.
     */
    public static void upsertBloodType(String patientID) {
        ensureFileExistsWithHeader();

        MedicalRecord medicalRecord = loadMedicalRecord(patientID);
        if (medicalRecord == null) {
            System.out.println("Medical record not found for patient ID: " + patientID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(patientID);
        }

        String newBloodType = InputHandler.getValidatedInput(
                "Enter new Blood Type (e.g., A+, B-, O+): ",
                "Invalid blood type. Must be A+, A-, B+, B-, AB+, AB-, O+, O-.",
                input -> input.matches("^(A|B|AB|O)[+-]$")
        );

        try {
            medicalRecord.setBloodType(newBloodType);
            saveMedicalRecord(medicalRecord);
            System.out.println("Blood type updated successfully for patient ID: " + patientID);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Updates or adds a diagnosis in the medical record and saves the changes to the CSV file.
     * If the medical record does not exist, it creates a new one.
     *
     * @param patientID the ID of the patient whose diagnosis needs to be updated.
     */
    public static void upsertDiagnosis(String patientID) {
        ensureFileExistsWithHeader();

        MedicalRecord medicalRecord = loadMedicalRecord(patientID);
        if (medicalRecord == null) {
            System.out.println("Medical record not found for patient ID: " + patientID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(patientID);
        }

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

        Diagnosis newDiagnosis = new Diagnosis(condition, diagnosisDate, prescription);

        List<Diagnosis> diagnoses = new ArrayList<>(medicalRecord.getDiagnoses());

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

        medicalRecord.addDiagnoses(diagnoses);
        saveMedicalRecord(medicalRecord);
        System.out.println("Diagnosis " + (updated ? "updated" : "added") + " successfully for patient ID: " + patientID);
    }

    /**
     * Adds or updates a diagnosis in the medical record for the given patient ID.
     * If a diagnosis already exists for the condition, it is updated; otherwise, a new one is added.
     *
     * @param patientID the ID of the patient whose diagnosis needs to be updated.
     */
    public static void upsertDiagnosis(String patientID, Diagnosis newDiagnosis) {
        ensureFileExistsWithHeader();  // Ensures that the file with the correct headers exists

        // Load the medical record for the given patient ID
        MedicalRecord medicalRecord = loadMedicalRecord(patientID);
        if (medicalRecord == null) {
            System.out.println("Medical record not found for patient ID: " + patientID);
            System.out.println("Creating a new medical record...");
            medicalRecord = new MedicalRecord(patientID);  // Create a new medical record if none exists
        }

        // Get the list of existing diagnoses from the medical record
        List<Diagnosis> diagnoses = new ArrayList<>(medicalRecord.getDiagnoses());

        // Flag to track if the diagnosis was updated
        boolean updated = false;

        // Check if the diagnosis already exists, and update it if necessary
        for (int i = 0; i < diagnoses.size(); i++) {
            if (diagnoses.get(i).getCondition().equalsIgnoreCase(newDiagnosis.getCondition())) {
                diagnoses.set(i, newDiagnosis);  // Update existing diagnosis with the new one
                updated = true;
                break;
            }
        }

        if (!updated) {
            diagnoses.add(newDiagnosis);
        }

        medicalRecord.addDiagnoses(diagnoses);
        saveMedicalRecord(medicalRecord);
        System.out.println("Diagnosis " + (updated ? "updated" : "added") + " successfully for patient ID: " + patientID);
    }

    // Helpers --

    /**
     * Ensures the medical records file exists and creates it with a header if it doesn't.
     */
    private static void ensureFileExistsWithHeader() {
        File file = new File(MEDICAL_RECORDS_FILE);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType,Diagnoses");
                bw.newLine();
                System.out.println("Medical records file created with headers: " + MEDICAL_RECORDS_FILE);
            } catch (IOException e) {
                System.err.println("Error creating medical records file: " + e.getMessage());
            }
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
                    .append(diagnosis.getPrescription()).append(";");
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
     * @param diagnosesData the serialized diagnoses HMS.data.
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
            if (parts.length == 3) {
                diagnoses.add(new Diagnosis(parts[0], parts[1], parts[2]));
            } else {
                System.err.println("Invalid diagnosis entry: " + entry);
            }
        }
        return diagnoses;
    }

    // Data  --
    /**
     * Saves or updates a medical record in the CSV file.
     *
     * @param medicalRecord the medical record to save or update.
     */
    public static void saveMedicalRecord(MedicalRecord medicalRecord) {
        ensureFileExistsWithHeader();

        File file = new File(MEDICAL_RECORDS_FILE);
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
     * Loads a medical record for a given patient ID.
     *
     * @param patientID the ID of the patient whose record needs to be loaded.
     * @return the medical record if found; null otherwise.
     */
    public static MedicalRecord loadMedicalRecord(String patientID) {
        ensureFileExistsWithHeader();

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICAL_RECORDS_FILE))) {
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
}