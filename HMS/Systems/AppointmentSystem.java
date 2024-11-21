package HMS.Systems;

import HMS.Enums.AppointmentStatus;
import HMS.Models.Appointment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A system to manage user appointments, including the ability to create, update, and cancel appointments.
 * It allows users to schedule and view appointments, as well as manage appointments for specific users.
 */
public class AppointmentSystem {

    private static final String DOCTOR_AVAILABILITY_FILE = "HMS/data/doctor_availability.csv";
    private static final String APPOINTMENTS_FILE = "HMS/data/appointments.csv";
    private static final List<Appointment> appointments = new ArrayList<>();
    private static final Map<String, List<String>> doctorAvailability = new HashMap<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Static initializer block to load appointment and doctor availabilities when the class is first accessed.
     * Ensures that the DoctorAvailability and Appointments are loaded into memory at runtime.
     */
    static {
        initializeFiles();
        loadDoctorAvailability();
        loadAppointments();
    }

    /**
     * Initializes the required files by checking if they exist.
     * If they don't, the files are created with the appropriate header information.
     * This method ensures that the following files are initialized:
     * - Doctor Availability file with the header: "DoctorID,AvailableSlots"
     * - Appointments file with the header: "ID,PatientID,DoctorID,Status,Date"
     */
    private static void initializeFiles() {
        ensureFileExists(DOCTOR_AVAILABILITY_FILE, "DoctorID,AvailableSlots");
        ensureFileExists(APPOINTMENTS_FILE, "ID,PatientID,DoctorID,Status,Date");
    }

    /**
     * Ensures that a file exists at the specified file path. If the file does not exist, it is created
     * along with the provided header information. If necessary, the parent directories of the file are also created.
     *
     * @param filePath The path of the file to be checked and created if it doesn't exist.
     * @param header The header content to be written into the file if it is created.
     */
    private static void ensureFileExists(String filePath, String header) {
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                file.getParentFile().mkdirs();
                bw.write(header);
                bw.newLine();
                System.out.println(filePath + " created with headers.");
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath + " - " + e.getMessage());
            }
        }
    }

    // -- Doctor Availability Management
    /**
     * Prompts the user to input an available time slot for a given doctor and adds it to the doctor's availability list.
     * The input is validated to ensure it is in the correct format (YYYY-MM-DD HH:mm).
     * The time is also checked to ensure it falls within the valid hours (09:00 to 17:00).
     * If the slot is already in the availability list, it is not added again.
     *
     * @param doctorID The ID of the doctor for whom the availability is being set.
     */
    public static void addDoctorAvailabilityMenu(String doctorID) {
        String slot = InputHandler.getValidatedInput(
                "Enter an available slot (YYYY-MM-DD HH:mm): ",
                "Invalid format. Use YYYY-MM-DD HH:mm.",
                input -> input.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")
        );

        try {
            Date date = DATE_FORMAT.parse(slot);
            String formattedSlot = DATE_FORMAT.format(date);

            if (!isValidHour(date)) {
                System.out.println("Time must be between 09:00 and 17:00.");
                return;
            }

            List<String> availableSlots = doctorAvailability.getOrDefault(doctorID, new ArrayList<>());

            if (availableSlots.contains(formattedSlot)) {
                System.out.println("This slot is already in your availability list.");
                return;
            }

            addSlot(doctorID, formattedSlot);
            System.out.println("Availability added for Doctor ID: " + doctorID);
        } catch (Exception e) {
            System.out.println("Invalid slot. Please try again.");
        }
    }

    /**
     * Displays all doctors with their available slots in a table format.
     * Each doctor ID is displayed alongside their available time slots.
     * If a doctor has no available slots, a message stating "No slots available" is shown.
     */
    public static void displayAllDoctorsAvailability() {
        if (doctorAvailability.isEmpty()) {
            System.out.println("No availability set for any doctor.");
            return;
        }

        System.out.println("+------------+----------------------------------------------+");
        System.out.println("| Doctor ID  | Available Slots                              |");
        System.out.println("+------------+----------------------------------------------+");

        for (Map.Entry<String, List<String>> entry : doctorAvailability.entrySet()) {
            String doctorID = entry.getKey();
            List<String> slots = entry.getValue();
            if (slots.isEmpty()) {
                System.out.printf("| %-10s | %-44s |\n", doctorID, "No slots available");
            } else {
                System.out.printf("| %-10s | %-44s |\n", doctorID, slots.get(0));
                for (int i = 1; i < slots.size(); i++) {
                    System.out.printf("| %-10s | %-44s |\n", "", slots.get(i));
                }
            }
            System.out.println("+------------+----------------------------------------------+");
        }
    }

    /**
     * Displays the availability for a specific doctor based on the provided doctor ID.
     * If no availability is found for the doctor, a message is shown stating that no availability is set.
     *
     * @param doctorID The ID of the doctor whose availability is being displayed.
     */
    public static void displayDoctorAvailability(String doctorID) {
        List<String> slots = doctorAvailability.getOrDefault(doctorID, new ArrayList<>());
        if (slots.isEmpty()) {
            System.out.println("No availability set for Doctor ID: " + doctorID);
        } else {
            System.out.println("Available slots for Doctor ID: " + doctorID);
            displaySlots(slots);
        }
    }

    /**
     * Prompts the user to select a slot to remove from a given doctor's availability list.
     * If no availability is set for the doctor, a message is shown.
     * Once a valid slot is selected, it is removed from the list, and the changes are saved.
     *
     * @param doctorID The ID of the doctor whose availability is being modified.
     */
    public static void removeDoctorAvailability(String doctorID) {
        List<String> slots = doctorAvailability.get(doctorID);
        if (slots == null || slots.isEmpty()) {
            System.out.println("No availability set for Doctor ID: " + doctorID);
            return;
        }

        System.out.println("Select a slot to remove:");
        displaySlots(slots);

        String input = InputHandler.getValidatedInputWithExit(
                "Enter the number of the slot to remove or 'exit' to cancel: ",
                "Invalid selection.",
                value -> isValidSlotSelection(value, slots.size())
        );

        if (input != null) {
            int slotIndex = Integer.parseInt(input) - 1;
            String removedSlot = slots.remove(slotIndex);
            saveDoctorAvailability();
            System.out.println("Removed availability: " + removedSlot);
        }
    }

    // -- Appointment Management

    /**
     * Returns a list of all appointments.
     *
     * @return List of appointments.
     */
    public static List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Retrieves appointments for a specific patient filtered by status.
     *
     * @param patientID the ID of the patient.
     * @param statuses  the list of statuses to filter by (nullable for all statuses).
     * @return a list of matching appointments.
     */
    public static List<Appointment> getAppointmentsByPatient(String patientID, List<AppointmentStatus> statuses) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            boolean matchesPatient = appointment.getPatientID().equalsIgnoreCase(patientID);
            boolean matchesStatus = statuses == null || statuses.contains(appointment.getAppointmentStatus());

            if (matchesPatient && matchesStatus) {
                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Displays all appointments regardless of status or person.
     */
    public static void displayAllAppointments() {
        List<Appointment> allAppointments = getAppointments();  // Assuming getAppointments() returns all appointments

        if (allAppointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("\n--- All Appointments ---");
        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Doctor ID", "Date", "Status");
        System.out.println("------------------------------------------------------------");

        for (Appointment appointment : allAppointments) {
            System.out.printf("%-15d %-15s %-25s %-15s%n",
                    appointment.getID(),
                    appointment.getDoctorID(),
                    formatDate(appointment.getAppointmentDate()),
                    appointment.getAppointmentStatus());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Displays appointments for a specific patient filtered by status.
     *
     * @param patientID the ID of the patient.
     * @param statuses  the list of statuses to filter by (nullable for all statuses).
     */
    public static void displayAppointmentsByPatient(String patientID, List<AppointmentStatus> statuses) {
        List<Appointment> appointmentsForPatient = getAppointmentsByPatient(patientID, statuses);

        if (appointmentsForPatient.isEmpty()) {
            System.out.println("No appointments found for Patient ID: " + patientID);
            return;
        }

        System.out.println("\n--- Appointments for Patient ID: " + patientID + " ---");

        System.out.println("+-----------------+-----------------+-------------------------+-----------------+");
        System.out.printf("| %-15s | %-15s | %-23s | %-15s |%n", "Appointment ID", "Doctor ID", "Date", "Status");
        System.out.println("+-----------------+-----------------+-------------------------+-----------------+");

        for (Appointment appointment : appointmentsForPatient) {
            System.out.printf("| %-15d | %-15s | %-23s | %-15s |%n",
                    appointment.getID(),
                    appointment.getDoctorID(),
                    formatDate(appointment.getAppointmentDate()),
                    appointment.getAppointmentStatus());
        }

        System.out.println("+-----------------+-----------------+-------------------------+-----------------+");
    }

    /**
     * Displays a list of doctors with available slots and prompts the user to select a doctor to book an appointment with.
     * If no doctors have available slots, a message is displayed indicating this.
     * The method returns the selected doctor ID or null if no selection is made.
     *
     * @return The doctor ID of the selected doctor, or null if no valid doctor is selected or if there are no available doctors.
     */
    public static String selectDoctorWithAvailableSlots() {
        List<String> selectableDoctors = new ArrayList<>();
        System.out.printf("%-15s %-30s%n", "Doctor ID", "Available Slots");
        System.out.println("------------------------------------------------");

        for (Map.Entry<String, List<String>> entry : doctorAvailability.entrySet()) {
            String doctorID = entry.getKey();
            List<String> slots = entry.getValue();
            if (!slots.isEmpty()) {
                System.out.printf("%-15s %-30s%n", doctorID, String.join(", ", slots));
                selectableDoctors.add(doctorID);
            }
        }

        if (selectableDoctors.isEmpty()) {
            System.out.println("No doctors with available slots.");
            return null;
        }

        return InputHandler.getValidatedInput(
                "Enter the Doctor ID you want to book an appointment with: ",
                "Invalid input. Please select a valid Doctor ID.",
                selectableDoctors::contains
        );
    }

    /**
     * Displays the available slots for a specific doctor and prompts the user to select a slot for booking an appointment.
     * If the selected slot is not valid, the method returns null.
     * If no slots are available for the doctor, a message is displayed indicating so.
     *
     * @param doctorID The ID of the doctor for whom the appointment is being scheduled.
     * @return The selected appointment date, or null if the selection is invalid or if no available slots are found.
     */
    public static Date selectSlotForDoctor(String doctorID) {
        List<String> availableSlots = doctorAvailability.getOrDefault(doctorID, new ArrayList<>());

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for Doctor ID: " + doctorID);
            return null;
        }

        System.out.println("\nAvailable Slots for Doctor ID: " + doctorID);
        displaySlots(availableSlots);

        String selectedSlot = InputHandler.getValidatedInputWithExit(
                "Enter the slot number from the available options: ",
                "Invalid input. Please select a valid slot number.",
                input -> isValidSlotSelection(input, availableSlots.size())
        );

        if (selectedSlot == null) return null;

        try {
            return DATE_FORMAT.parse(availableSlots.get(Integer.parseInt(selectedSlot) - 1));
        } catch (Exception e) {
            System.out.println("Error parsing the selected slot.");
            return null;
        }
    }

    /**
     * Schedules an appointment for a patient with the specified doctor at the selected date and time.
     * If the selected slot is unavailable, an error message is displayed.
     * Once the appointment is scheduled, the slot is booked and the appointment is saved.
     *
     * @param patientID The ID of the patient scheduling the appointment.
     * @param doctorID The ID of the doctor with whom the appointment is being scheduled.
     * @param appointmentDate The date and time of the appointment.
     */
    public static void scheduleAppointment(String patientID, String doctorID, Date appointmentDate) {
        String slot = DATE_FORMAT.format(appointmentDate);
        if (!isSlotAvailable(doctorID, slot)) {
            System.out.println("The selected slot is not available.");
            return;
        }

        Appointment appointment = new Appointment(
                appointments.size() + 1,
                patientID,
                doctorID,
                AppointmentStatus.PENDING,
                appointmentDate
        );
        appointments.add(appointment);
        bookSlot(doctorID, slot);
        saveAppointments();
        System.out.println("Appointment scheduled successfully.");
    }

    /**
     * Reschedules an appointment to a new doctor and/or time slot.
     *
     * @param patientID the ID of the patient rescheduling the appointment.
     */
    public static void rescheduleAppointment(String patientID) {
        // Step 1: Get and display the patient's current appointments
        List<Appointment> patientAppointments = getAppointmentsByPatient(patientID, null);

        if (patientAppointments.isEmpty()) {
            System.out.println("You have no appointments to reschedule.");
            return;
        }

        System.out.println("\n--- Your Appointments ---");
        displayAppointmentsByPatient(patientID, null);

        // Step 2: Prompt the patient to select an appointment to reschedule
        String selectedAppointmentID = InputHandler.getValidatedInputWithExit(
                "Enter the Appointment ID to reschedule or 'cancel' to exit: ",
                "Invalid input. Please enter a valid appointment ID.",
                input -> {
                    try {
                        int id = Integer.parseInt(input);
                        return patientAppointments.stream().anyMatch(appointment -> appointment.getID() == id);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        if (selectedAppointmentID == null) {
            System.out.println("Rescheduling canceled.");
            return;
        }

        int appointmentID = Integer.parseInt(selectedAppointmentID);
        Appointment appointmentToReschedule = patientAppointments.stream()
                .filter(appointment -> appointment.getID() == appointmentID)
                .findFirst()
                .orElse(null);

        if (appointmentToReschedule == null) {
            System.out.println("Appointment not found.");
            return;
        }

        // Step 3: Display current appointment details
        System.out.println("\n--- Rescheduling Appointment ---");
        System.out.println("Current Appointment Details:");
        System.out.printf("Doctor: %s | Date: %s | Status: %s%n",
                appointmentToReschedule.getDoctorID(),
                formatDate(appointmentToReschedule.getAppointmentDate()),
                appointmentToReschedule.getAppointmentStatus()
        );

        // Step 4: Allow the patient to select a new doctor or slot
        boolean changeDoctor = InputHandler.getValidatedInput(
                "Do you want to select a different doctor? (yes/no): ",
                "Please enter 'yes' or 'no'.",
                input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")
        ).equalsIgnoreCase("yes");

        String newDoctorID = appointmentToReschedule.getDoctorID();
        if (changeDoctor) {
            newDoctorID = selectDoctorWithAvailableSlots();
            if (newDoctorID == null) {
                System.out.println("No doctors with available slots or selection canceled.");
                return;
            }
        }

        Date newSlot = selectSlotForDoctor(newDoctorID);
        if (newSlot == null) {
            System.out.println("No slots available or selection canceled.");
            return;
        }

        // Step 5: Update the appointment
        rescheduleAppointment(appointmentID, newDoctorID, newSlot);
    }

    /**
     * Updates the details of an appointment (reschedules it).
     *
     * @param appointmentID the ID of the appointment to reschedule.
     * @param newDoctorID   the new doctor's ID.
     * @param newDate       the new appointment date.
     */
    public static void rescheduleAppointment(int appointmentID, String newDoctorID, Date newDate) {
        String newSlot = DATE_FORMAT.format(newDate);

        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                if (!isSlotAvailable(newDoctorID, newSlot)) {
                    System.out.println("The new slot is not available. Please choose another.");
                    return;
                }

                // Free the old slot if the doctor changes
                if (!appointment.getDoctorID().equalsIgnoreCase(newDoctorID)) {
                    addSlot(appointment.getDoctorID(), formatDate(appointment.getAppointmentDate()));
                }

                // Book the new slot
                bookSlot(newDoctorID, newSlot);

                // Update the appointment details
                appointment.setDoctorID(newDoctorID);
                appointment.setAppointmentDate(newDate);
                saveAppointments();

                System.out.println("Appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    /**
     * Approves pending appointments for a specific doctor.
     *
     * @param doctorID the ID of the doctor.
     */
    public static void approvePendingAppointments(String doctorID) {
        boolean foundPending = false;

        System.out.println("\n--- Pending Appointments for Approval ---");
        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Patient ID", "Date", "Status");
        System.out.println("-------------------------------------------------------------------");

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equalsIgnoreCase(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {
                System.out.printf("%-15d %-15s %-25s %-15s%n",
                        appointment.getID(),
                        appointment.getPatientID(),
                        DATE_FORMAT.format(appointment.getAppointmentDate()),
                        appointment.getAppointmentStatus());
                foundPending = true;
            }
        }

        if (!foundPending) {
            System.out.println("No pending appointments to approve.");
            return;
        }

        String inputID = InputHandler.getValidatedInputWithExit(
                "Enter the Appointment ID to approve or type 'exit' to go back: ",
                "Invalid input. Please enter a valid Appointment ID.",
                input -> {
                    try {
                        int appointmentID = Integer.parseInt(input);
                        return appointments.stream().anyMatch(app ->
                                app.getID() == appointmentID &&
                                        app.getDoctorID().equalsIgnoreCase(doctorID) &&
                                        app.getAppointmentStatus() == AppointmentStatus.PENDING);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        if (inputID == null) {
            System.out.println("Approval process canceled.");
            return;
        }

        int appointmentID = Integer.parseInt(inputID);
        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID) {
                appointment.setAppointmentStatus(AppointmentStatus.APPROVED);
                saveAppointments();
                System.out.println("Appointment ID " + appointmentID + " has been approved successfully.");
                break;
            }
        }
    }

    /**
     * Cancels an appointment by its ID and returns the slot back to the doctor's availability.
     *
     * @param patientID     the ID of the patient.
     * @param appointmentID the ID of the appointment to be canceled.
     */
    public static void cancelAppointment(String patientID, int appointmentID) {
        // Find the appointment to cancel
        Appointment appointmentToCancel = null;

        for (Appointment appointment : appointments) {
            if (appointment.getID() == appointmentID && appointment.getPatientID().equalsIgnoreCase(patientID)) {
                appointmentToCancel = appointment;
                break;
            }
        }

        if (appointmentToCancel == null) {
            System.out.println("No matching appointment found for the provided ID.");
            return;
        }

        // Return the slot to the doctor's availability
        String doctorID = appointmentToCancel.getDoctorID();
        String slot = DATE_FORMAT.format(appointmentToCancel.getAppointmentDate());

        if (!isSlotAvailable(doctorID, slot)) {
            doctorAvailability.computeIfAbsent(doctorID, any -> new ArrayList<>()).add(slot);
            saveDoctorAvailability();
            System.out.println("Slot " + slot + " has been returned to availability for Doctor ID: " + doctorID);
        }

        // Mark the appointment as canceled
        appointmentToCancel.setAppointmentStatus(AppointmentStatus.CANCELLED);
        saveAppointments(); // Save the updated appointments to the file

        System.out.println("Appointment ID " + appointmentID + " has been canceled successfully.");
    }

    /**
     * Displays upcoming appointments (today and future) for a specific doctor.
     *
     * @param doctorID the ID of the doctor.
     */
    public static void viewUpcomingAppointments(String doctorID) {
        boolean foundUpcoming = false;

        Date today = new Date();

        System.out.println("\n--- Upcoming Appointments ---");
        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Patient ID", "Date", "Status");
        System.out.println("-------------------------------------------------------------------");

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equalsIgnoreCase(doctorID) &&
                    appointment.getAppointmentDate().compareTo(today) >= 0) {
                System.out.printf("%-15d %-15s %-25s %-15s%n",
                        appointment.getID(),
                        appointment.getPatientID(),
                        DATE_FORMAT.format(appointment.getAppointmentDate()),
                        appointment.getAppointmentStatus());
                foundUpcoming = true;
            }
        }

        if (!foundUpcoming) {
            System.out.println("No upcoming appointments found.");
        }
    }

    /**
     * Retrieves all appointments for a specific doctor.
     *
     * @param doctorID the ID of the doctor whose appointments need to be retrieved.
     * @param status   (optional) the status of the appointments to filter by. Pass `null` to retrieve all statuses.
     * @return a list of appointments for the specified doctor.
     */
    public static List<Appointment> getAppointmentsByDoctor(String doctorID, AppointmentStatus status) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            boolean matchesDoctor = appointment.getDoctorID().equalsIgnoreCase(doctorID);
            boolean matchesStatus = (status == null) || appointment.getAppointmentStatus() == status;

            if (matchesDoctor && matchesStatus) {
                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Displays all appointments for a specific doctor.
     *
     * @param doctorID the ID of the doctor whose appointments will be displayed.
     */
    public static void displayAppointmentsByDoctor(String doctorID, AppointmentStatus status) {
        List<Appointment> doctorAppointments = getAppointmentsByDoctor(doctorID, status);

        if (doctorAppointments.isEmpty()) {
            System.out.println("No appointments found for Doctor ID: " + doctorID);
            return;
        }

        System.out.println("\n--- Appointments for Doctor ID: " + doctorID + " ---");
        System.out.printf("%-15s %-15s %-25s %-15s%n", "Appointment ID", "Patient ID", "Date", "Status");
        System.out.println("-------------------------------------------------------------------");

        for (Appointment appointment : doctorAppointments) {
            System.out.printf("%-15d %-15s %-25s %-15s%n",
                    appointment.getID(),
                    appointment.getPatientID(),
                    DATE_FORMAT.format(appointment.getAppointmentDate()),
                    appointment.getAppointmentStatus());
        }
        System.out.println("-------------------------------------------------------------------");
    }

    // -- Utility Methods
    /**
     * Validates whether the provided input corresponds to a valid slot selection within the given size.
     * The input must be a number between 1 and the size of available slots.
     *
     * @param input The input to validate.
     * @param size The total number of available slots.
     * @return True if the input is a valid slot selection, false otherwise.
     */
    private static boolean isValidSlotSelection(String input, int size) {
        try {
            int index = Integer.parseInt(input);
            return index > 0 && index <= size;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates whether the provided date is within the working hours (9:00 AM to 5:00 PM).
     *
     * @param date The date to validate.
     * @return True if the time is within the valid range (9:00 AM to 5:00 PM), false otherwise.
     */
    private static boolean isValidHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour >= 9 && hour < 17;
    }

    /**
     * Adds a new available slot for the given doctor and saves the updated availability.
     *
     * @param doctorID The ID of the doctor for whom the slot is being added.
     * @param slot The new available slot to be added in the format "yyyy-MM-dd HH:mm".
     */
    private static void addSlot(String doctorID, String slot) {
        doctorAvailability.computeIfAbsent(doctorID, k -> new ArrayList<>()).add(slot);
        saveDoctorAvailability();
    }

    /**
     * Displays the available slots in a tabular format.
     * If no slots are available, a message will be displayed instead.
     *
     * @param slots The list of available slots to display.
     */
    private static void displaySlots(List<String> slots) {
        if (slots.isEmpty()) {
            System.out.println("No slots available.");
            return;
        }

        System.out.println("+-----+-----------------------+");
        System.out.println("| No. | Available Slot        |");
        System.out.println("+-----+-----------------------+");

        for (int i = 0; i < slots.size(); i++) {
            System.out.printf("| %-3d | %-21s |\n", i + 1, slots.get(i));
        }

        System.out.println("+-----+-----------------------+");
    }

    /**
     * Checks whether the given slot is available for the specified doctor.
     *
     * @param doctorID The ID of the doctor to check availability for.
     * @param slot The slot to check availability for.
     * @return True if the slot is available, false otherwise.
     */
    private static boolean isSlotAvailable(String doctorID, String slot) {
        List<String> slots = doctorAvailability.get(doctorID);
        return slots != null && slots.contains(slot);
    }

    /**
     * Formats a given Date object into a readable string in the format "yyyy-MM-dd HH:mm".
     *
     * @param date the Date object to format.
     * @return the formatted date string.
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "N/A"; // Handle null dates
        }
        return DATE_FORMAT.format(date);
    }

    /**
     * Books a slot for the specified doctor by removing it from their available slots.
     * After booking, the updated availability is saved.
     *
     * @param doctorID The ID of the doctor whose slot is being booked.
     * @param slot The slot to be booked in the format "yyyy-MM-dd HH:mm".
     */
    private static void bookSlot(String doctorID, String slot) {
        List<String> slots = doctorAvailability.get(doctorID);
        if (slots != null) {
            slots.remove(slot);
            saveDoctorAvailability();
        }
    }

    /**
     * Loads the doctor availability HMS.data from a file and populates the `doctorAvailability` map.
     * The HMS.data is expected to be in CSV format, with doctor IDs and their available slots.
     */
    private static void loadDoctorAvailability() {
        try (BufferedReader br = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    doctorAvailability.put(parts[0], new ArrayList<>(Arrays.asList(parts[1].split(";"))));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading doctor availability: " + e.getMessage());
        }
    }

    /**
     * Saves doctor availability to a file, ensuring no empty slots are saved.
     */
    private static void saveDoctorAvailability() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOCTOR_AVAILABILITY_FILE))) {
            bw.write("DoctorID,AvailableSlots");
            bw.newLine();

            for (Map.Entry<String, List<String>> entry : doctorAvailability.entrySet()) {
                // Filter out empty or null slots
                List<String> validSlots = entry.getValue().stream()
                        .filter(slot -> slot != null && !slot.trim().isEmpty())
                        .toList();

                bw.write(entry.getKey() + "," + String.join(";", validSlots));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving doctor availability: " + e.getMessage());
        }
    }

    /**
     * Loads the appointments from a file and populates the `appointments` list.
     * The HMS.data is expected to be in CSV format with appointment details.
     */
    private static void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String patientID = parts[1];
                String doctorID = parts[2];
                AppointmentStatus status = AppointmentStatus.valueOf(parts[3].toUpperCase());
                Date date = DATE_FORMAT.parse(parts[4]);
                appointments.add(new Appointment(id, patientID, doctorID, status, date));
            }
        } catch (Exception e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of appointments to a file.
     * The HMS.data is saved in CSV format with appointment details, including the status and date.
     */
    public static void saveAppointments() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            bw.write("ID,PatientID,DoctorID,Status,Date");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(String.join(",",
                        String.valueOf(appointment.getID()),
                        appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getAppointmentStatus().toString(),
                        DATE_FORMAT.format(appointment.getAppointmentDate())));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }
}