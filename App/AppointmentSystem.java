import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class handles appointment scheduling, updating, and saving appointments.
 * It also loads and displays appointments for a specific user (patient or doctor).
 * Appointments are saved as text files in the `data/appointments/` folder with the filename format `patientID-doctorID-Date.txt`.
 */
public class AppointmentSystem {

    // List of all appointment filenames in the system
    private static List<String> appointments = new ArrayList<>();

    // Directory path for storing appointment files
    private static final String APPOINTMENT_DIR = "data/appointments/";

    /**
     * Schedules a new appointment with the specified details.
     *
     * @param patientID the ID of the patient
     * @param doctorID the ID of the doctor
     * @param appointmentDate the date and time of the appointment
     */
    public static void scheduleAppointment(String patientID, String doctorID, Date appointmentDate) {
        Appointment newAppointment = new Appointment(patientID, doctorID, "confirmed", appointmentDate);
        saveAppointmentToFile(newAppointment);
    }

    /**
     * Updates the status of an existing appointment.
     *
     * @param patientID the ID of the patient
     * @param doctorID the ID of the doctor
     * @param appointmentDate the date of the appointment
     * @param status the new status of the appointment
     */
    public static void updateAppointmentStatus(String patientID, String doctorID, Date appointmentDate, String status) {
        for (String fileName : appointments) {
            if (fileName.contains(patientID) && fileName.contains(doctorID) && fileName.contains(formatDate(appointmentDate))) {
                // Load the appointment from the file
                Appointment appointment = loadAppointmentFromFile(fileName);
                appointment.setAppointmentStatus(status);
                saveAppointmentToFile(appointment);
                return;
            }
        }
    }

    /**
     * Cancels an existing appointment.
     *
     * @param patientID the ID of the patient
     * @param doctorID the ID of the doctor
     * @param appointmentDate the date of the appointment
     */
    public static void cancelAppointment(String patientID, String doctorID, Date appointmentDate) {
        for (String fileName : appointments) {
            if (fileName.contains(patientID) && fileName.contains(doctorID) && fileName.contains(formatDate(appointmentDate))) {
                // Load the appointment from the file
                Appointment appointment = loadAppointmentFromFile(fileName);
                appointment.setAppointmentStatus("canceled");
                saveAppointmentToFile(appointment);
                return;
            }
        }
    }

    /**
     * Adds an appointment outcome for a completed appointment
     * Specifically for doctors
     *
     * @param patientID the ID of the patient
     * @param doctorID the ID of the doctor
     * @param appointmentDate the date of the appointment
     * @param outcomeRecord the appointment outcome record
     */
    public static void addAppointmentOutcome(String patientID, String doctorID, Date appointmentDate, AppointmentOutcomeRecord outcomeRecord) {
        for (String fileName : appointments) {
            if (fileName.contains(patientID) && fileName.contains(doctorID) && fileName.contains(formatDate(appointmentDate))) {
                // Load the appointment from the file
                Appointment appointment = loadAppointmentFromFile(fileName);
                appointment.setOutcomeRecord(outcomeRecord);
                saveAppointmentToFile(appointment);
                return;
            }
        }
    }

    /**
     * Displays all appointments for a specific user (patient or doctor).
     *
     * @param userID the ID of the user (patient or doctor)
     */
    public static void displayAppointments(String userID) {
        for (String fileName : appointments) {
            if (fileName.contains(userID)) {
                // Load and display the appointment from the file
                Appointment appointment = loadAppointmentFromFile(fileName);
                System.out.println("Patient ID: " + appointment.getPatientID());
                System.out.println("Doctor ID: " + appointment.getDoctorID());
                System.out.println("Status: " + appointment.getAppointmentStatus());
                System.out.println("Date: " + appointment.getAppointmentDate());
                if (appointment.getOutcomeRecord() != null) {
                    System.out.println("Appointment Outcome: " + appointment.getOutcomeRecord().getConsultationNotes());
                }
                System.out.println("-----------");
            }
        }
    }

    /**
     * Saves an appointment to a file with the filename as `patientID-doctorID-Date.txt`.
     *
     * @param appointment the appointment to save
     */
    private static void saveAppointmentToFile(Appointment appointment) {
        String fileName = appointment.getPatientID() + "-" + appointment.getDoctorID() + "-" + formatDate(appointment.getAppointmentDate()) + ".txt";
        File appointmentDir = new File(APPOINTMENT_DIR);
        if (!appointmentDir.exists()) {
            appointmentDir.mkdirs(); // Ensure the appointments directory exists
        }
        File file = new File(appointmentDir, fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(appointment);
            appointments.add(fileName); // Add the filename to the list of appointments
        } catch (IOException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    /**
     * Loads an appointment from a file.
     *
     * @param fileName the name of the file containing the appointment
     * @return the appointment object
     */
    private static Appointment loadAppointmentFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(APPOINTMENT_DIR + fileName))) {
            return (Appointment) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading appointment from file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Formats the date of the appointment into a string (YYYY-MM-DD).
     *
     * @param date the date to format
     * @return the formatted date string
     */
    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Loads all the appointment files in the directory and adds them to the appointments list.
     */
    public static void loadAllAppointments() {
        File appointmentDir = new File(APPOINTMENT_DIR);
        File[] listOfFiles = appointmentDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                appointments.add(file.getName());
            }
        }
    }
}
