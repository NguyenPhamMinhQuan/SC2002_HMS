package Models;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Handles appointment scheduling, updating, and viewing.
 * Stores appointments in memory and in a CSV file.
 */
public class AppointmentSystem {

    private static final String APPOINTMENT_CSV = "data/appointments.csv";
    private static final List<Appointment> appointments = new ArrayList<>();

    static {
        loadAllAppointments();
    }

    /**
     * Displays available slots for doctors.
     * (Integration with `Doctor` class for dynamic slot generation.)
     */
    public static void displayAvailableSlots() {
        Map<String, List<String>> doctorSlots = new HashMap<>();
        doctorSlots.put("Dr001", Arrays.asList("2024-11-19 10:00", "2024-11-19 11:00", "2024-11-19 14:00"));
        doctorSlots.put("Dr002", Arrays.asList("2024-11-20 09:00", "2024-11-20 13:00", "2024-11-20 15:00"));

        System.out.println("Available Appointment Slots:");
        for (Map.Entry<String, List<String>> entry : doctorSlots.entrySet()) {
            System.out.println("Doctor ID: " + entry.getKey());
            for (String slot : entry.getValue()) {
                System.out.println("  Slot: " + slot);
            }
        }
    }

    /**
     * Schedule an appointment.
     */
    public static void scheduleAppointment(String patientID, String doctorID, Date appointmentDate) {
        Appointment appointment = new Appointment(appointments.size() + 1, patientID, doctorID, "confirmed", appointmentDate);
        appointments.add(appointment);
        saveAllAppointments();
        System.out.println("Appointment scheduled successfully!");
    }

    /**
     * Update an appointment's date.
     */
    public static void updateAppointment(int appointmentID, Date newDate) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentDate(newDate);
                saveAllAppointments();
                System.out.println("Appointment updated successfully!");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Cancel an appointment.
     */
    public static void cancelAppointment(int appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentStatus("canceled");
                saveAllAppointments();
                System.out.println("Appointment canceled successfully!");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Display appointments by patient and status.
     */
    public static void displayAppointmentsByPatient(String patientID, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean found = false;

        // Print table header
        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Doctor ID", "Date", "Status");
        System.out.println("-----------------------------------------------------------------------------------");

        for (Appointment appointment : appointments) {
            boolean matchesPatient = appointment.getPatientID().equals(patientID);
            boolean matchesStatus = status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status);

            if (matchesPatient && matchesStatus) {
                // Print each appointment as a table row
                System.out.printf("%-15d %-15s %-25s %-15s%n",
                        appointment.getID(),
                        appointment.getDoctorID(),
                        sdf.format(appointment.getAppointmentDate()),
                        appointment.getAppointmentStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }
    }


    /**
     * Load all appointments.
     */
    private static void loadAllAppointments() {
        File file = new File(APPOINTMENT_CSV);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String patientID = fields[1];
                String doctorID = fields[2];
                String status = fields[3];
                Date date = parseDate(fields[4]);
                appointments.add(new Appointment(id, patientID, doctorID, status, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all appointments.
     */
    private static void saveAllAppointments() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_CSV))) {
            bw.write("ID,PatientID,DoctorID,AppointmentStatus,AppointmentDate");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(String.format("%d,%s,%s,%s,%s",
                        appointment.getID(),
                        appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getAppointmentStatus(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(appointment.getAppointmentDate())));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse date from string.
     */
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
