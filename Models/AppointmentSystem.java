package Models;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class handles appointment scheduling, updating, and saving appointments.
 * All appointments are stored in a single CSV file for simplicity and scalability.
 */
public class AppointmentSystem {

    // CSV file path for storing appointments
    private static final String APPOINTMENT_CSV = "data/appointments.csv";

    // List to hold all appointments in memory
    private static final List<Appointment> appointments = new ArrayList<>();

    /**
     * Schedules a new appointment with the specified details.
     *
     * @param patientID       the ID of the patient
     * @param doctorID        the ID of the doctor
     * @param appointmentDate the date and time of the appointment
     */
    public static void scheduleAppointment(String patientID, String doctorID, Date appointmentDate) {
        Appointment newAppointment = new Appointment(appointments.size() + 1, patientID, doctorID, "confirmed", appointmentDate);
        appointments.add(newAppointment);
        saveAllAppointments();
    }

    /**
     * Updates the status of an existing appointment.
     *
     * @param appointmentID the ID of the appointment
     * @param status        the new status of the appointment
     */
    public static void updateAppointmentStatus(int appointmentID, String status) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentStatus(status);
                saveAllAppointments();
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Updates the date of an existing appointment.
     *
     * @param appointmentID the ID of the appointment to update
     * @param newDate       the new appointment date
     */
    public static void updateAppointment(int appointmentID, Date newDate) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                System.out.println("Rescheduling appointment...");
                appointment.setAppointmentDate(newDate); // Update the date
                saveAllAppointments(); // Save the updated appointments to the CSV file
                System.out.println("Appointment rescheduled successfully!");
                displayAppointmentDetails(appointment, new SimpleDateFormat("yyyy-MM-dd")); // Display updated appointment details
                return;
            }
        }
        System.out.println("Appointment with ID " + appointmentID + " not found.");
    }


    /**
     * Cancels an existing appointment.
     *
     * @param appointmentID the ID of the appointment
     */
    public static void cancelAppointment(int appointmentID) {
        updateAppointmentStatus(appointmentID, "canceled");
    }

    /**
     * Adds an appointment outcome for a completed appointment.
     *
     * @param appointmentID the ID of the appointment
     * @param outcomeRecord the appointment outcome record
     */
    public static void addAppointmentOutcome(int appointmentID, AppointmentOutcomeRecord outcomeRecord) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setOutcomeRecord(outcomeRecord);
                saveAllAppointments();
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Retrieves appointments for a specific doctor filtered by status.
     *
     * @param doctorID the ID of the doctor
     * @param status   the status of the appointments to filter by (nullable)
     * @return a list of matching appointments
     */
    public static List<Appointment> getAppointmentsByDoctor(String doctorID, String status) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            boolean matchesDoctor = appointment.getDoctorID().equalsIgnoreCase(doctorID);
            boolean matchesStatus = (status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status));

            if (matchesDoctor && matchesStatus) {
                result.add(appointment);
            }
        }
        return result;
    }


    /**
     * Displays appointments based on the provided filter.
     * If all parameters are null, it displays all appointments.
     *
     * @param doctorId the patient ID to filter by
     * @param status   the appointment status to filter by (nullable)
     */
    public static void displayAppointmentsByDoctor(String doctorId, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Appointment appointment : appointments) {
            boolean matchesDoctor = appointment.getDoctorID().equalsIgnoreCase(doctorId);
            boolean matchesStatus = (status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status));

            if (matchesDoctor && matchesStatus) {
                displayAppointmentDetails(appointment, sdf);
            }
        }
    }

    /**
     * Displays appointments based on the provided filter.
     * If all parameters are null, it displays all appointments.
     *
     * @param patientID the patient ID to filter by
     * @param status    the appointment status to filter by (nullable)
     */
    public static void displayAppointmentsByPatient(String patientID, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Appointment appointment : appointments) {
            boolean matchesPatient = appointment.getPatientID().equals(patientID);
            boolean matchesStatus = (status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status));

            if (matchesPatient && matchesStatus) {
                displayAppointmentDetails(appointment, sdf);
            }
        }
    }


    private static void displayAppointmentDetails(Appointment appointment, SimpleDateFormat sdf) {
        System.out.println("Appointment ID: " + appointment.getID());
        System.out.println("Patient ID: " + appointment.getPatientID());
        System.out.println("Doctor ID: " + appointment.getDoctorID());
        System.out.println("Status: " + appointment.getAppointmentStatus());
        System.out.println("Date: " + sdf.format(appointment.getAppointmentDate()));
        if (appointment.getOutcomeRecord() != null) {
            System.out.println("Outcome Notes: " + appointment.getOutcomeRecord().getConsultationNotes());
        }
        System.out.println("-----------");
    }


    /**
     * Saves all appointments to the CSV file.
     */
    public static void saveAllAppointments() {
        File appointmentFile = new File(APPOINTMENT_CSV);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(appointmentFile))) {
            // Write header
            bw.write("ID,PatientID,DoctorID,AppointmentStatus,AppointmentDate,OutcomeNotes,ServiceType,PrescribedMedications");
            bw.newLine();

            // Write each appointment
            for (Appointment appointment : appointments) {
                StringBuilder csvLine = new StringBuilder();
                csvLine.append(appointment.getID()).append(",")
                        .append(appointment.getPatientID()).append(",")
                        .append(appointment.getDoctorID()).append(",")
                        .append(appointment.getAppointmentStatus()).append(",")
                        .append(formatDate(appointment.getAppointmentDate())).append(",");

                if (appointment.getOutcomeRecord() != null) {
                    csvLine.append(appointment.getOutcomeRecord().getConsultationNotes()).append(",")
                            .append(appointment.getOutcomeRecord().getServiceType()).append(",");

                    // Serialize prescribed medications into a single string
                    List<Medication> meds = appointment.getOutcomeRecord().getPrescribedMedications();
                    for (Medication med : meds) {
                        csvLine.append(med.getMedicationName()).append("-").append(med.getStatus()).append(";");
                    }
                }
                bw.write(csvLine.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    /**
     * Loads all appointments from the CSV file.
     */
    public static void loadAllAppointments() {
        File appointmentFile = new File(APPOINTMENT_CSV);
        if (!appointmentFile.exists()) {
            System.out.println("No appointment file found. Starting fresh.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentFile))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String patientID = fields[1];
                String doctorID = fields[2];
                String status = fields[3];
                Date date = parseDate(fields[4]);

                Appointment appointment = new Appointment(id, patientID, doctorID, status, date);

                if (fields.length > 5 && !fields[5].isEmpty()) {
                    String outcomeNotes = fields[5];
                    String serviceType = fields[6];
                    assert date != null;
                    AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(date.toString(), serviceType, outcomeNotes);

                    if (fields.length > 7) {
                        String[] medications = fields[7].split(";");
                        for (String med : medications) {
                            String[] medDetails = med.split("-");
                            outcome.addMedication(new Medication(medDetails[0], medDetails[1]));
                        }
                    }
                    appointment.setOutcomeRecord(outcome);
                }
                appointments.add(appointment);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    /**
     * Formats the date of the appointment into a string (YYYY-MM-DD).
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Parses a date string in the format YYYY-MM-DD into a Date object.
     *
     * @param dateString the date string
     * @return the Date object
     */
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateString);
        } catch (Exception e) {
            System.out.println("Error parsing date: " + dateString);
            return null;
        }
    }

}
