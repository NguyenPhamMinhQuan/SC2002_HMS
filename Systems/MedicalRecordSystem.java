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
     * Saves or updates a medical record in the CSV file.
     *
     * @param medicalRecord the medical record to save or update.
     */
    public static void saveMedicalRecord(MedicalRecord medicalRecord) {
        File file = new File(MEDICAL_RECORD_CSV);
        List<String> allRecords = new ArrayList<>();
        boolean recordUpdated = false;

        try {
            // Read all existing records
            if (file.exists()) {
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
            }

            // If the record was not found, add it as a new record
            if (!recordUpdated) {
                if (file.length() == 0) {
                    allRecords.add("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType,Diagnoses"); // Add header
                }
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
     * Serializes a medical record into a CSV-compatible string.
     *
     * @param medicalRecord the medical record to serialize.
     * @return a string representing the serialized medical record.
     */
    private static String serializeMedicalRecord(MedicalRecord medicalRecord) {
        StringBuilder recordLine = new StringBuilder();
        recordLine.append(medicalRecord.getPatientID()).append(",");
        recordLine.append(medicalRecord.getDateOfBirth()).append(",");
        recordLine.append(medicalRecord.getPhoneNumber()).append(",");
        recordLine.append(medicalRecord.getEmailAddress()).append(",");
        recordLine.append(medicalRecord.getBloodType()).append(",");

        // Serialize diagnoses
        List<Diagnosis> diagnoses = medicalRecord.getPastDiagnoses();
        for (Diagnosis diagnosis : diagnoses) {
            recordLine.append(diagnosis.getCondition()).append("|")
                    .append(diagnosis.getDiagnosisDate()).append("|")
                    .append(diagnosis.getPrescription()).append("|")
                    .append(diagnosis.getPrescriptionStatus()).append(";");
        }

        return recordLine.toString();
    }

    /**
     * Loads a medical record for a given patient ID.
     *
     * @param patientID the ID of the patient whose record needs to be loaded.
     * @return the medical record if found; null otherwise.
     */
    public static MedicalRecord loadMedicalRecord(String patientID) {
        File file = new File(MEDICAL_RECORD_CSV);

        // Check if the file exists; if not, create it with headers
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType,Diagnoses");
                bw.newLine();
                System.out.println("Medical records file created with headers: " + MEDICAL_RECORD_CSV);
            } catch (IOException e) {
                System.err.println("Error creating medical records file: " + e.getMessage());
                return null;
            }
        }

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
                    String dateOfBirth = recordDetails[1].isEmpty() ? "Unknown" : recordDetails[1];
                    String phoneNumber = recordDetails[2].isEmpty() ? "Unknown" : recordDetails[2];
                    String emailAddress = recordDetails[3].isEmpty() ? "Unknown" : recordDetails[3];
                    String bloodType = recordDetails[4].isEmpty() ? "Unknown" : recordDetails[4];

                    // Create and populate the medical record
                    MedicalRecord medicalRecord = new MedicalRecord(patientID);
                    medicalRecord.setDateOfBirth(dateOfBirth);
                    medicalRecord.setPhoneNumber(phoneNumber);
                    medicalRecord.setEmailAddress(emailAddress);
                    medicalRecord.setBloodType(bloodType);

                    // Parse diagnoses if present and not `None`
                    if (recordDetails.length > 5 && !recordDetails[5].equalsIgnoreCase("None") && !recordDetails[5].isEmpty()) {
                        String[] diagnoses = recordDetails[5].split(";");
                        for (String diagnosisDetail : diagnoses) {
                            try {
                                String[] diagnosisParts = diagnosisDetail.split("\\|");
                                if (diagnosisParts.length == 4) {
                                    Diagnosis diagnosis = new Diagnosis(
                                            diagnosisParts[0], // Condition
                                            diagnosisParts[1], // Date
                                            diagnosisParts[2], // Prescription
                                            diagnosisParts[3]  // Status
                                    );
                                    medicalRecord.addDiagnosis(diagnosis);
                                } else {
                                    System.err.println("Invalid diagnosis entry: " + diagnosisDetail);
                                }
                            } catch (Exception e) {
                                System.err.println("Error parsing diagnosis: " + diagnosisDetail);
                            }
                        }
                    }

                    return medicalRecord; // Return the loaded record
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        System.err.println("Medical record not found for Patient ID: " + patientID);
        return null; // Return null if not found
    }
}