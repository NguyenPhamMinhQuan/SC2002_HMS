package Repositories;
import Enums.Dispensed;
import Models.AppointmentOutcomeRecord;
import Models.Medication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRepository implements IAppointmentOutcomeRepository {
    private static final String OUTCOMES_FILE = "data/appointment_outcomes.csv";
    private final List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();

    public AppointmentOutcomeRepository() {
        loadOutcomes();
    }

    @Override
    public List<AppointmentOutcomeRecord> getAllOutcomes() {
        return new ArrayList<>(outcomes); // Return a copy to avoid external modifications
    }

    @Override
    public AppointmentOutcomeRecord getOutcomeById(int appointmentID) {
        return outcomes.stream()
                .filter(outcome -> outcome.getAppointmentID() == appointmentID)
                .findFirst()
                .orElse(null); // Return null if no matching outcome is found
    }

    @Override
    public void saveOutcomes(List<AppointmentOutcomeRecord> outcomes) {
        File file = new File(OUTCOMES_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Write the header
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes,Dispensed,DoctorID,PatientID");
            bw.newLine();

            // Write each outcome as a line in the file
            for (AppointmentOutcomeRecord outcome : outcomes) {
                bw.write(formatOutcome(outcome));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointment outcomes: " + e.getMessage());
        }
    }

    private AppointmentOutcomeRecord parseOutcome(String line) {
        String[] parts = line.split(",", 8);
        if (parts.length < 8) {
            return null;
        }

        try {
            int appointmentID = Integer.parseInt(parts[0]);
            String appointmentDate = parts[1];
            String serviceType = parts[2];
            String medicationsStr = parts[3];
            String consultationNotes = parts[4];
            Dispensed dispensed = Dispensed.valueOf(parts[5].toUpperCase());
            String doctorID = parts[6];
            String patientID = parts[7];

            AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                    appointmentID,
                    appointmentDate,
                    serviceType,
                    consultationNotes,
                    dispensed,
                    doctorID,
                    patientID
            );

            // Parse medications
            if (!medicationsStr.isEmpty()) {
                String[] medications = medicationsStr.split(";");
                for (String med : medications) {
                    String[] medParts = med.split(" \\(");
                    String name = medParts[0].trim();
                    int quantity = Integer.parseInt(medParts[1].replace(")", "").trim());
                    outcome.addMedication(new Medication(name, "pending", quantity));
                }
            }

            return outcome;

        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.err.println("Error parsing outcome line: " + line + ". Error: " + e.getMessage());
        }

        return null;
    }

    private String formatOutcome(AppointmentOutcomeRecord outcome) {
        return String.join(",",
                String.valueOf(outcome.getAppointmentID()),
                outcome.getAppointmentDate(),
                outcome.getServiceType(),
                outcome.getMedicationsAsString(),
                outcome.getConsultationNotes(),
                outcome.isDispensed().toString(),
                outcome.getDoctorID(),
                outcome.getPatientID()
        );
    }

    private void loadOutcomes() {
        File file = new File(OUTCOMES_FILE);

        if (!file.exists()) {
            createOutcomesFile(file);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                AppointmentOutcomeRecord outcome = parseOutcome(line);
                if (outcome != null) {
                    outcomes.add(outcome);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointment outcomes: " + e.getMessage());
        }
    }

    private void createOutcomesFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes,Dispensed,DoctorID,PatientID");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error creating outcomes file: " + e.getMessage());
        }
    }
}
