import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages the reading and saving of medical records for patients.
 * Each patient's record is saved as a .txt file named by their patientID.
 */
public class MedicalRecordSystem {

    private static final String DIRECTORY_PATH = "medical_records"; // Folder to store medical records
    private Map<String, MedicalRecord> records = new HashMap<>();

    // Constructor
    public MedicalRecordSystem() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdir(); // Create directory if it doesn't exist
        }
    }

    /**
     * Saves the medical record of a patient as a .txt file named by their patientID.
     * 
     * @param medicalRecord the medical record to save.
     */
    public static void saveMedicalRecord(MedicalRecord medicalRecord) {
        String fileName = DIRECTORY_PATH + File.separator + medicalRecord.getPatientID() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Medical Record for Patient ID: " + medicalRecord.getPatientID() + "\n");
            writer.write("Date of Birth: " + medicalRecord.getDateOfBirth() + "\n");
            writer.write("Phone Number: " + medicalRecord.getPhoneNumber() + "\n");
            writer.write("Email: " + medicalRecord.getEmailAddress() + "\n");
            writer.write("Blood Type: " + medicalRecord.getBloodType() + "\n");

            writer.write("\nDiagnoses and Treatments:\n");
            writer.write("-------------------------------------------------------------------------------\n");
            writer.write(String.format("%-20s %-15s %-25s %-20s\n", "Diagnosis", "Date", "Prescription", "Prescription Status"));
            writer.write("-------------------------------------------------------------------------------\n");

            for (Diagnosis diagnosis : medicalRecord.getPastDiagnoses()) {
                writer.write(String.format("%-20s %-15s %-25s %-20s\n", 
                              diagnosis.getCondition(), 
                              diagnosis.getDiagnosisDate(), 
                              diagnosis.getPrescription(),
                              diagnosis.getPrescriptionStatus()));
            }
            writer.write("-------------------------------------------------------------------------------\n");

        } catch (IOException e) {
            System.out.println("Error saving medical record for patient ID: " + medicalRecord.getPatientID());
            e.printStackTrace();
        }
    }

    /**
     * Loads a medical record from a .txt file based on the patientID.
     * 
     * @param patientID the patient ID of the record to load.
     * @return the MedicalRecord object if found; otherwise, null.
     */
    public static MedicalRecord loadMedicalRecord(String patientID) {
        String fileName = DIRECTORY_PATH + File.separator + patientID + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String bloodType = "";
                String dateOfBirth = "";
                String phoneNumber = "";
                String emailAddress = "";
                MedicalRecord record = new MedicalRecord(patientID);

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Date of Birth: ")) {
                        dateOfBirth = line.substring(15);
                    } else if (line.startsWith("Phone Number: ")) {
                        phoneNumber = line.substring(14);
                    } else if (line.startsWith("Email: ")) {
                        emailAddress = line.substring(7);
                    } else if (line.startsWith("Blood Type: ")) {
                        bloodType = line.substring(12);
                    } else if (line.startsWith("Diagnosis")) {
                        continue; // Skip header line
                    } else if (line.contains("-")) {
                        // Parse diagnosis
                        
                        String[] diagnosisDetails = line.split("\\|");
                        if (diagnosisDetails.length >= 4) {
                            // Create a Diagnosis object with the parsed data
                            Diagnosis diagnosis = new Diagnosis(diagnosisDetails[0], diagnosisDetails[1], diagnosisDetails[2], diagnosisDetails[3]);
                            // Add the diagnosis to the patient's record
                            record.addDiagnosis(diagnosis);
                        } else {
                            System.out.println("Invalid diagnosis line: " + line);
                        }
                    }
                }
                record.setDateOfBirth(dateOfBirth);
                record.setPhoneNumber(phoneNumber);
                record.setEmailAddress(emailAddress);
                record.setBloodType(bloodType);
                return record;

            } catch (IOException e) {
                System.out.println("Error reading medical record for patient ID: " + patientID);
                e.printStackTrace();
            }
        } else {
            System.out.println("Medical record not found for patient ID: " + patientID);
        }

        return null; // Return null if the file doesn't exist
    }

    /**
     * Retrieves the stored medical records.
     * 
     * @return the map of patient IDs to medical records.
     */
    public Map<String, MedicalRecord> getRecords() {
        return records;
    }

}
