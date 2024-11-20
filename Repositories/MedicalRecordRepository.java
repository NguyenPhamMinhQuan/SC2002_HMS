package HMS.Repositories;

import HMS.Models.MedicalRecord;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordRepository implements LoadandSaveInterface<MedicalRecord> {

    private static final String MEDICAL_RECORD_CSV = "data/medical_records.csv";

    @Override
    public List<MedicalRecord> loadData() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICAL_RECORD_CSV))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] recordDetails = line.split(",");
                String patientID = recordDetails[0];
                String dateOfBirth = recordDetails[1];
                String phoneNumber = recordDetails[2];
                String emailAddress = recordDetails[3];
                String bloodType = recordDetails[4];
                
                MedicalRecord medicalRecord = new MedicalRecord(patientID, dateOfBirth, phoneNumber, emailAddress, bloodType);
                medicalRecords.add(medicalRecord);
                
            }
        } catch (IOException e) {
            System.err.println("Error loading medical records: " + e.getMessage());
        }
        return medicalRecords;
    }

    @Override
    public void saveData(List<MedicalRecord> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICAL_RECORD_CSV))) {
            bw.write("PatientID,DateOfBirth,PhoneNumber,EmailAddress,BloodType");
            bw.newLine();
            for (MedicalRecord record : data) {
                bw.write(String.join(",",
                        record.getPatientID(),
                        record.getDateOfBirth(),
                        record.getPhoneNumber(),
                        record.getEmailAddress(),
                        record.getBloodType()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving medical records: " + e.getMessage());
        }
    }
}
