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

    public static MedicalRecord loadMedicalRecord(String patientID) {
        File file = new File(MEDICAL_RECORD_CSV);
        if (!file.exists()) {
            System.out.println("Medical record file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // Skip the header line
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) {
                    System.err.println("Invalid record: " + line);
                    continue;
                }

                if (fields[0].equals(patientID)) {
                    MedicalRecord record = new MedicalRecord(patientID);
                    record.setDateOfBirth(fields[1]);
                    record.setPhoneNumber(fields[2]);
                    record.setEmailAddress(fields[3]);
                    record.setBloodType(fields[4]);

                    if (fields.length > 5) {
                        String[] diagnoses = fields[5].split(";");
                        for (String diagnosisStr : diagnoses) {
                            String[] diagnosisFields = diagnosisStr.split("\\|");
                            if (diagnosisFields.length == 4) {
                                Diagnosis diagnosis = new Diagnosis(
                                        diagnosisFields[0],
                                        diagnosisFields[1],
                                        diagnosisFields[2],
                                        diagnosisFields[3]
                                );
                                record.addDiagnosis(diagnosis);
                            }
                        }
                    }

                    return record;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading medical record file: " + e.getMessage());
        }

        System.out.println("Medical record for Patient ID " + patientID + " not found.");
        return null;
    }

}
