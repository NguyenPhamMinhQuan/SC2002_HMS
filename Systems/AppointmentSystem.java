package Systems;

import Models.Appointment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppointmentSystem {

    private static final String DOCTOR_AVAILABILITY_FILE = "data/doctor_availability.csv";
    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final List<Appointment> appointments = new ArrayList<>();
    private static final Map<String, List<String>> doctorAvailability = new HashMap<>();

    static {
        initializeDoctorAvailabilityFile();
        loadDoctorAvailability();
        loadAppointments();
    }

    /**
     * Displays appointments by doctor.
     */
    public static void displayAppointmentsByDoctor(String doctorID, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean found = false;

        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Patient ID", "Date", "Status");
        System.out.println("-------------------------------------------------------------------");

        for (Appointment appointment : appointments) {
            boolean matchesDoctor = appointment.getDoctorID().equalsIgnoreCase(doctorID);
            boolean matchesStatus = status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status);

            if (matchesDoctor && matchesStatus) {
                System.out.printf("%-15d %-15s %-25s %-15s%n",
                        appointment.getID(),
                        appointment.getPatientID(),
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
     * Displays appointments by patient.
     */
    public static void displayAppointmentsByPatient(String patientID, List<String> statuses) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean found = false;

        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Doctor ID", "Date", "Status");
        System.out.println("-------------------------------------------------------------------");

        for (Appointment appointment : appointments) {
            boolean matchesPatient = appointment.getPatientID().equalsIgnoreCase(patientID);
            boolean matchesStatus = statuses == null || statuses.contains(appointment.getAppointmentStatus());

            if (matchesPatient && matchesStatus) {
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
     * Schedules an appointment.
     */
    public static void scheduleAppointment(String patientID, String doctorID, Date appointmentDate) {
        String slot = formatDate(appointmentDate);

//        if (!isSlotAvailable(doctorID, slot)) {
//            System.out.println("The selected slot is not available. Please choose another slot.");
//            return;
//        }

        Appointment appointment = new Appointment(
                appointments.size() + 1,
                patientID,
                doctorID,
                "pending",
                appointmentDate
        );
        appointments.add(appointment);

        bookSlot(doctorID, slot);
        saveAppointments();

        System.out.println("Appointment request created successfully.");
    }

    /**
     * Updates the status of an appointment.
     */
    public static void updateAppointmentStatus(int appointmentID, String status) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentStatus(status);
                saveAppointments();
                System.out.println("Appointment status updated to " + status + ".");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Updates an appointment to a new date.
     */
    public static void updateAppointment(int appointmentID, Date newDate) {
        String newSlot = formatDate(newDate);
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                if (!isSlotAvailable(appointment.getDoctorID(), newSlot)) {
                    System.out.println("The new slot is not available. Please choose another.");
                    return;
                }

                bookSlot(appointment.getDoctorID(), newSlot);
                appointment.setAppointmentDate(newDate);
                saveAppointments();
                System.out.println("Appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Cancels an appointment.
     */
    public static void cancelAppointment(int appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentStatus("canceled");
                saveAppointments();
                System.out.println("Appointment canceled successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Sets availability for a doctor.
     */
    public static void setAvailability(String doctorID) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTimeFormat.setLenient(false);

        System.out.println("Enter available slots (e.g., 2024-12-01 09:00,2024-12-01 10:00):");
        String input = scanner.nextLine();
        String[] slots = input.split(",");

        for (String slot : slots) {
            try {
                Date date = dateTimeFormat.parse(slot.trim());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                if (hour < 9 || hour > 16) {
                    System.out.println("Invalid time: " + slot + ". Must be between 09:00 and 16:00.");
                } else {
                    toggleSlotAvailability(doctorID, formatDate(date));
                }
            } catch (Exception e) {
                System.out.println("Invalid date format for slot: " + slot + ". Use YYYY-MM-DD HH:mm.");
            }
        }
        saveDoctorAvailability();
    }

    /**
     * Displays available slots for all doctors.
     */
    public static void displayAvailableSlots() {
        for (Map.Entry<String, List<String>> entry : doctorAvailability.entrySet()) {
            System.out.println("Doctor ID: " + entry.getKey());
            for (String slot : entry.getValue()) {
                System.out.println("  Slot: " + slot);
            }
        }
    }

    /**
     * Checks if a slot is available.
     */
    public static boolean isSlotAvailable(String doctorID, String slot) {
        List<String> slots = doctorAvailability.get(doctorID);
        return slots != null && slots.contains(slot);
    }

    /**
     * Toggles slot availability.
     */
    private static void toggleSlotAvailability(String doctorID, String slot) {
        List<String> slots = doctorAvailability.getOrDefault(doctorID, new ArrayList<>());

        if (slots.contains(slot)) {
            slots.remove(slot);
            System.out.println("Removed availability: " + slot);
        } else {
            slots.add(slot);
            System.out.println("Added availability: " + slot);
        }
        doctorAvailability.put(doctorID, slots);
    }

    /**
     * Books a slot for an appointment.
     */
    private static void bookSlot(String doctorID, String slot) {
        List<String> slots = doctorAvailability.get(doctorID);
        if (slots != null) {
            slots.remove(slot);
            saveDoctorAvailability();
        }
    }

    /**
     * Formats a date into a slot string.
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * Loads doctor availability from a file.
     */
    private static void loadDoctorAvailability() {
        File file = new File(DOCTOR_AVAILABILITY_FILE);
        if (!file.exists()) {
            System.out.println("Doctor availability file not found. Starting fresh.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String doctorID = parts[0];
                    List<String> slots = Arrays.asList(parts[1].split(";"));
                    doctorAvailability.put(doctorID, new ArrayList<>(slots));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading doctor availability: " + e.getMessage());
        }
    }

    /**
     * Saves doctor availability to a file.
     */
    private static void saveDoctorAvailability() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOCTOR_AVAILABILITY_FILE))) {
            bw.write("DoctorID,Availability");
            bw.newLine();

            for (Map.Entry<String, List<String>> entry : doctorAvailability.entrySet()) {
                bw.write(entry.getKey() + "," + String.join(";", entry.getValue()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save doctor availability: " + e.getMessage());
        }
    }

    /**
     * Loads appointments from a file.
     */
    private static void loadAppointments() {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String patientID = parts[1];
                String doctorID = parts[2];
                String status = parts[3];
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(parts[4]);

                appointments.add(new Appointment(id, patientID, doctorID, status, date));
            }
        } catch (Exception e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    /**
     * Saves appointments to a file.
     */
    private static void saveAppointments() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            bw.write("ID,PatientID,DoctorID,Status,Date");
            bw.newLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (Appointment appointment : appointments) {
                bw.write(String.join(",",
                        String.valueOf(appointment.getID()),
                        appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getAppointmentStatus(),
                        sdf.format(appointment.getAppointmentDate())));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save appointments: " + e.getMessage());
        }
    }

    /**
     * Initializes the doctor availability file if it does not exist.
     */
    private static void initializeDoctorAvailabilityFile() {
        File file = new File(DOCTOR_AVAILABILITY_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error initializing doctor availability file: " + e.getMessage());
            }
        }
    }

    public static List<String> getDoctorAvailability(String doctorID) {
        return doctorAvailability.getOrDefault(doctorID, new ArrayList<>());
    }

    /**
     * Retrieves appointments for a specific doctor filtered by status.
     *
     * @param doctorID the ID of the doctor.
     * @param status   the status of the appointments to filter by (nullable for all statuses).
     * @return a list of matching appointments.
     */
    public static List<Appointment> getAppointmentsByDoctor(String doctorID, String status) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            boolean matchesDoctor = appointment.getDoctorID().equalsIgnoreCase(doctorID);
            boolean matchesStatus = status == null || appointment.getAppointmentStatus().equalsIgnoreCase(status);

            if (matchesDoctor && matchesStatus) {
                result.add(appointment);
            }
        }

        return result;
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
