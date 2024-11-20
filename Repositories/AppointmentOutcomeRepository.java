package HMS.Repositories;

import HMS.Enums.Dispensed;
import HMS.Models.AppointmentOutcomeRecord;
import HMS.Models.Medication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRepository implements LoadandSaveInterface<AppointmentOutcomeRecord> {

    private static final String OUTCOMES_FILE = "data/appointment_outcomes.csv";

    @Override
    public List<AppointmentOutcomeRecord> loadData() {
        List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(OUTCOMES_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                int appointmentID = Integer.parseInt(details[0]);
                String date = details[1];
                String serviceType = details[2];
                String medications = details[3];
                String consultationNotes = details[4];
                
                // Here we use Dispensed.valueOf to convert the string value to the appropriate Dispensed enum.
                Dispensed dispensed = Dispensed.valueOf(details[5].toUpperCase()); 
                
                String doctorID = details[6];
                String patientID = details[7];

                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                        appointmentID, date, serviceType, consultationNotes, dispensed, doctorID, patientID
                );

                // Add medications if available
                if (!medications.isEmpty()) {
                    String[] meds = medications.split(";");
                    for (String med : meds) {
                        String[] medParts = med.split(" \\(");
                        String name = medParts[0].trim();
                        int quantity = Integer.parseInt(medParts[1].replace(")", "").trim());
                        outcome.addMedication(new Medication(name, "pending", quantity));
                    }
                }
                outcomes.add(outcome);
            }
        } catch (IOException e) {
            System.err.println("Error loading outcomes: " + e.getMessage());
        }
        return outcomes;
    }
    @Override
    public void saveData(List<AppointmentOutcomeRecord> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTCOMES_FILE))) {
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes,Dispensed,DoctorID,PatientID");
            bw.newLine();
    
            for (AppointmentOutcomeRecord outcome : data) {
                // Get medications as a formatted string
                String medicationsAsString = outcome.getMedicationsAsString();
    
                // Writing the record to CSV
                bw.write(String.join(",",
                        String.valueOf(outcome.getAppointmentID()),
                        outcome.getAppointmentDate(),
                        outcome.getServiceType(),
                        medicationsAsString, // Medications should be a string now
                        outcome.getConsultationNotes(),
                        outcome.isDispensed().toString(), // Dispensed status converted to string
                        outcome.getDoctorID(),
                        outcome.getPatientID()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving outcomes: " + e.getMessage());
        }
    }
    
}
