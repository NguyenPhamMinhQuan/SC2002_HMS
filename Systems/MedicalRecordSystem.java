package Systems;

import Models.Diagnosis;
import Models.MedicalRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordSystem {

    private static final String MEDICAL_RECORD_CSV = "data/medical_records.csv";

    /**
     * Saves or updates a medical record in the CSV file.
     *
     * @param medicalRecord The medical record to save or update.
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
            System.out.println("Error saving medical record: " + e.getMessage());
        }
    }

    /**
     * Serializes a medical record into a CSV-compatible string.
     *
     * @param medicalRecord The medical record to serialize.
     * @return A string representing the serialized medical record.
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
}
