package Systems;

import Models.Appointment;
import Models.AppointmentOutcomeRecord;
import Models.Medication;
import Models.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeSystem {
    private static final String OUTCOMES_FILE = "data/appointment_outcomes.csv";
    private static final List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();

    static {

        loadOutcomes();
    }
    public AppointmentOutcomeSystem() {
        loadOutcomes();
    }

    /**
     * Adds a new appointment outcome.
     */
    public static void addOutcome(AppointmentOutcomeRecord outcome) {
        outcomes.add(outcome);
        saveOutcomes();
    }

    private static Appointment getAppointmentByID(int appointmentID) {
        List<Appointment> appointments = AppointmentSystem.getAppointments(); // Get all appointments
        return appointments.stream()
                .filter(appointment -> appointment.getID() == appointmentID)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an outcome by appointment ID.
     *
     * @param appointmentID the ID of the appointment.
     * @return the corresponding outcome, or null if not found.
     */
    public AppointmentOutcomeRecord getOutcomeByAppointmentID(int appointmentID) {
        for (AppointmentOutcomeRecord outcome : outcomes) {
            if (outcome.getAppointmentID() == appointmentID) {
                return outcome;
            }
        }
        return null;
    }

    /**
     * Loads all outcomes from the CSV file.
     */
    private static void loadOutcomes() {
        File file = new File(OUTCOMES_FILE);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes");
                bw.newLine();
            } catch (IOException e) {
                System.err.println("Error creating outcomes file: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                int appointmentID = Integer.parseInt(parts[0]);
                String appointmentDate = parts[1];
                String serviceType = parts[2];
                String medicationsStr = parts[3];
                String consultationNotes = parts[4];

                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                        appointmentID, appointmentDate, serviceType, consultationNotes
                );

                if (!medicationsStr.isEmpty()) {
                    String[] medications = medicationsStr.split(";");
                    for (String med : medications) {
                        String[] medParts = med.split(" \\("); // Name and quantity
                        String name = medParts[0].trim();
                        int quantity = Integer.parseInt(medParts[1].replace(")", "").trim());
                        outcome.addMedication(new Medication(name, "pending", quantity));
                    }
                }

                outcomes.add(outcome);
            }
        } catch (Exception e) {
            System.err.println("Error loading outcomes: " + e.getMessage());
        }
    }

    /**
     * Saves all outcomes to the CSV file.
     */
    private static void saveOutcomes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTCOMES_FILE))) {
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes");
            bw.newLine();

            for (AppointmentOutcomeRecord outcome : outcomes) {
                bw.write(String.join(",",
                        String.valueOf(outcome.getAppointmentID()),
                        outcome.getAppointmentDate(),
                        outcome.getServiceType(),
                        outcome.getMedicationsAsString(),
                        outcome.getConsultationNotes()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving outcomes: " + e.getMessage());
        }
    }


    /**
     * Allows a doctor to add outcomes for their approved appointments.
     *
     * @param doctorID the doctor's ID.
     */
    public static void addOutcomeByDoctor(String doctorID) {
        List<Appointment> approvedAppointments = AppointmentSystem.getAppointmentsByDoctor(doctorID, "approved");

        if (approvedAppointments.isEmpty()) {
            System.out.println("No approved appointments available to add outcomes.");
            return;
        }

        System.out.println("\n--- Approved Appointments for Doctor ID: " + doctorID + " ---");
        AppointmentSystem.displayAppointmentsByDoctor(doctorID, "approved");

        // Prompt the doctor to select an appointment
        String selectedAppointmentID = InputHandler.getValidatedInput(
                "Enter the Appointment ID to add an outcome: ",
                "Invalid input. Please enter a valid Appointment ID.",
                input -> {
                    try {
                        int id = Integer.parseInt(input);
                        return approvedAppointments.stream().anyMatch(appointment -> appointment.getID() == id);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        if (selectedAppointmentID == null) {
            System.out.println("Outcome creation canceled.");
            return;
        }

        int appointmentID = Integer.parseInt(selectedAppointmentID);
        Appointment appointment = approvedAppointments.stream()
                .filter(app -> app.getID() == appointmentID)
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        // Prompt for service type
        String serviceType = InputHandler.getValidatedInput(
                "Enter the service type (e.g., Consultation, X-Ray, etc.): ",
                "Service type cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        // Prompt for consultation notes
        String consultationNotes = InputHandler.getValidatedInput(
                "Enter consultation notes: ",
                "Notes cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        // Create the outcome record
        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                appointmentID,
                AppointmentSystem.formatDate(appointment.getAppointmentDate()),
                serviceType,
                consultationNotes
        );

        // Add medications (optional)
        boolean addMedications = InputHandler.getValidatedInput(
                "Do you want to add prescribed medications? (yes/no): ",
                "Please enter 'yes' or 'no'.",
                input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")
        ).equalsIgnoreCase("yes");

        if (addMedications) {
            addMedicationsToOutcome(outcome);
        }

        // Save the outcome
        addOutcome(outcome);
        System.out.println("Appointment outcome record added successfully.");
    }

    private static void addMedicationsToOutcome(AppointmentOutcomeRecord outcome) {
        StockSystem stockSystem = new StockSystem();
        boolean addMore = true;

        while (addMore) {
            // Display available stock in a table format
            List<Stock> availableStocks = stockSystem.getStocks();
            if (availableStocks.isEmpty()) {
                System.out.println("No available stock found.");
                return;
            }

            System.out.println("+-----+--------------------------+---------------+");
            System.out.println("| No. | Medicine Name            | Stock Level   |");
            System.out.println("+-----+--------------------------+---------------+");

            for (int i = 0; i < availableStocks.size(); i++) {
                Stock stock = availableStocks.get(i);
                System.out.printf("| %-3d | %-24s | %-13d |\n",
                        i + 1,
                        stock.getMedicineName(),
                        stock.getStockLevel());
            }

            System.out.println("+-----+--------------------------+---------------+");

            // Prompt the user to select a stock by its number
            String selectedStockIndex = InputHandler.getValidatedInput(
                    "Enter the number of the medicine to prescribe (or type 'exit' to finish): ",
                    "Invalid input. Please enter a valid number or 'exit'.",
                    input -> input.equalsIgnoreCase("exit") || isValidStockSelection(input, availableStocks.size())
            );

            if (selectedStockIndex.equalsIgnoreCase("exit")) {
                System.out.println("Finished adding medications.");
                break;
            }

            int stockIndex = Integer.parseInt(selectedStockIndex) - 1;
            Stock selectedStock = availableStocks.get(stockIndex);

            // Prompt the user for the quantity
            int maxQuantity = selectedStock.getStockLevel();
            String quantityInput = InputHandler.getValidatedInput(
                    "Enter the quantity to prescribe (max " + maxQuantity + "): ",
                    "Invalid quantity. Must be a number between 1 and " + maxQuantity + ".",
                    input -> {
                        try {
                            int quantity = Integer.parseInt(input);
                            return quantity > 0 && quantity <= maxQuantity;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
            );

            int quantity = Integer.parseInt(quantityInput);

            // Add the selected medication to the outcome
            Medication medication = new Medication(selectedStock.getMedicineName(), "pending", quantity);
            outcome.addMedication(medication);

            System.out.println("Added " + quantity + " units of " + selectedStock.getMedicineName() + " to the outcome.");

            // Prompt the user if they want to add more medications
            addMore = InputHandler.getValidatedInput(
                    "Do you want to add more medications? (yes/no): ",
                    "Please enter 'yes' or 'no'.",
                    input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")
            ).equalsIgnoreCase("yes");
        }
    }


    public static void displayOutcomesForDoctor(String doctorID) {
        List<AppointmentOutcomeRecord> doctorOutcomes = new ArrayList<>();

        for (AppointmentOutcomeRecord outcome : outcomes) {
            Appointment appointment = getAppointmentByID(outcome.getAppointmentID());
            if (appointment != null && appointment.getDoctorID().equalsIgnoreCase(doctorID)) {
                doctorOutcomes.add(outcome);
            }
        }

        if (doctorOutcomes.isEmpty()) {
            System.out.println("No outcomes found for Doctor ID: " + doctorID);
            return;
        }

        System.out.println("\n--- Appointment Outcomes for Doctor ID: " + doctorID + " ---");
        displayOutcomeTable(doctorOutcomes);
    }

    public static void displayOutcomesForPatient(String patientID) {
        List<AppointmentOutcomeRecord> patientOutcomes = new ArrayList<>();

        for (AppointmentOutcomeRecord outcome : outcomes) {
            Appointment appointment = getAppointmentByID(outcome.getAppointmentID());
            if (appointment != null && appointment.getPatientID().equalsIgnoreCase(patientID)) {
                patientOutcomes.add(outcome);
            }
        }

        if (patientOutcomes.isEmpty()) {
            System.out.println("No outcomes found for Patient ID: " + patientID);
            return;
        }

        System.out.println("\n--- Appointment Outcomes for Patient ID: " + patientID + " ---");
        displayOutcomeTable(patientOutcomes);
    }

    public static void displayAllOutcomes() {
        if (outcomes.isEmpty()) {
            System.out.println("No outcomes recorded.");
            return;
        }

        System.out.println("\n--- All Appointment Outcomes ---");
        displayOutcomeTable(outcomes);
    }

    private static void displayOutcomeTable(List<AppointmentOutcomeRecord> outcomesList) {
        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+");
        System.out.println("| AppointmentID | Date              | Service Type      | Medications                     | Consultation Notes     |");
        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+");

        for (AppointmentOutcomeRecord outcome : outcomesList) {
            System.out.printf("| %-13d | %-17s | %-17s | %-31s | %-21s |\n",
                    outcome.getAppointmentID(),
                    outcome.getAppointmentDate(),
                    outcome.getServiceType(),
                    outcome.getMedicationsAsString(),
                    outcome.getConsultationNotes());
        }

        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+");
    }


    /**
     * Validates if the user input corresponds to a valid stock selection.
     *
     * @param input the user input.
     * @param size  the total number of available stocks.
     * @return true if the input is valid, false otherwise.
     */
    private static boolean isValidStockSelection(String input, int size) {
        try {
            int index = Integer.parseInt(input);
            return index > 0 && index <= size;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}