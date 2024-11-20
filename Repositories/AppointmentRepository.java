package Repositories;

import Enums.AppointmentStatus;
import Models.Appointment;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentRepository implements IAppointmentRepository {
    private static final String FILE_PATH = "data/appointments.csv"; // Path to the appointment data file

    @Override
    public List<Appointment> getAllAppointments() {
        return loadData(); // Delegate to loadData() for getting all appointments
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId, AppointmentStatus status) {
        return loadData().stream()
                .filter(appointment -> appointment.getDoctorId().equals(doctorId) && appointment.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return loadData().stream()
                .filter(appointment -> appointment.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public Appointment findById(int appointmentId) {
        return loadData().stream()
                .filter(appointment -> appointment.getId() == appointmentId)
                .findFirst()
                .orElse(null); // Return null if not found
    }

    @Override
    public void saveAllAppointments(List<Appointment> appointments) {
        saveData(appointments); // Delegate to saveData() for saving the appointments
    }

    /**
     * Loads appointment data from the CSV file.
     * 
     * @return a list of appointments loaded from the file.
     */
    private List<Appointment> loadData() {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.err.println("File not found: " + FILE_PATH);
            return appointments; // Return empty list if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                Appointment appointment = parseAppointment(line);
                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }

        return appointments;
    }

    /**
     * Saves the list of appointments to the CSV file.
     * 
     * @param appointments the list of appointments to save.
     */
    private void saveData(List<Appointment> appointments) {
        File file = new File(FILE_PATH);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Write the CSV header
            bw.write("AppointmentID,DoctorID,PatientID,Date,Status");
            bw.newLine();

            // Write each appointment's data as a new line in the CSV
            for (Appointment appointment : appointments) {
                bw.write(formatAppointment(appointment));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    /**
     * Parses an individual line from the CSV and creates an Appointment object.
     * 
     * @param line the CSV line to parse.
     * @return the parsed Appointment object or null if the line is invalid.
     */
    private Appointment parseAppointment(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 5) {
            return null;
        }

        try {
            int appointmentId = Integer.parseInt(parts[0]);
            String doctorId = parts[1];
            String patientId = parts[2];
            String date = parts[3];
            AppointmentStatus status = AppointmentStatus.valueOf(parts[4].toUpperCase());

            return new Appointment(appointmentId, doctorId, patientId, date, status);
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing appointment line: " + line + ". Error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Formats an Appointment object into a CSV line.
     * 
     * @param appointment the Appointment object to format.
     * @return the formatted CSV line.
     */
    private String formatAppointment(Appointment appointment) {
        return String.join(",",
                String.valueOf(appointment.getId()),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getDate(),
                appointment.getStatus().toString()
        );
    }
}
