package Repositories;

import Models.MedicalRecord;
import Models.Diagnosis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordRepository implements LoadandSaveInterface<MedicalRecord> {

    private static final String MEDICAL_RECORD_CSV = "data/medical_records.csv";

    @Override
    public List<MedicalRecord> loadData() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        File file = new File(MEDICAL_RECORD_CSV);

        if (!file.exists()) {
            return medicalRecords; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) continue; // Skip invalid lines

                String patientID = fields[0];
                String dateOfBirth = fields[1];
                String phoneNumber = fields[2];
                String emailAddress = fields[3];
                String bloodType = fields[4];

                // Deserialize diagnoses
                List<Diagnosis> diagnoses = deserializeDiagnoses(fields.length > 5 ? fields[5] : "");

                MedicalRecord medicalRecord = new MedicalRecord(patientID);
                medicalRecord.setDateOfBirth(dateOfBirth);
                medicalRecord.setPhoneNumber(phoneNumber);
                medicalRecord.setEmailAddress(emailAddress);
                medicalRecord.setBloodType(bloodType);
                medicalRecord.addDiagnoses(diagnoses);

                medicalRecords.add(medicalRecord);
            }
        } catch (IOException e) {
            System.err.println("Error loading medical records: " + e.getMessage());
        }

        return medicalRecords;
    }

    @Override
    public void saveData(List<MedicalRecord> medicalRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEDICAL_RECORD_CSV))) {
            // Write header
            writer.write("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType,Diagnoses");
            writer.newLine();

            for (MedicalRecord medicalRecord : medicalRecords) {
                writer.write(serializeMedicalRecord(medicalRecord));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving medical records: " + e.getMessage());
        }
    }

    private String serializeMedicalRecord(MedicalRecord medicalRecord) {
        return String.join(",",
                medicalRecord.getPatientID(),
                medicalRecord.getDateOfBirth(),
                medicalRecord.getPhoneNumber(),
                medicalRecord.getEmailAddress(),
                medicalRecord.getBloodType(),
                serializeDiagnoses(medicalRecord.getDiagnoses())
        );
    }

    private String serializeDiagnoses(List<Diagnosis> diagnoses) {
        StringBuilder serialized = new StringBuilder();
        for (Diagnosis diagnosis : diagnoses) {
            serialized.append(diagnosis.getCondition()).append("|")
                    .append(diagnosis.getDiagnosisDate()).append("|")
                    .append(diagnosis.getPrescription()).append(";");
        }
        return serialized.toString();
    }

    private List<Diagnosis> deserializeDiagnoses(String diagnosesData) {
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

    public MedicalRecord findByPatientID(String patientID) {
        List<MedicalRecord> medicalRecords = loadData();
        for (MedicalRecord record : medicalRecords) {
            if (record.getPatientID().equals(patientID)) {
                return record;
            }
        }
        return null;
    }
}
