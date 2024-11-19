package Systems;

import Models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Systems.StockSystem.getStocks;

public class AppointmentOutcomeSystem {
    private static final String OUTCOMES_FILE = "data/appointment_outcomes.csv";
    private static final List<AppointmentOutcomeRecord> outcomes = new ArrayList<>();

    static {
        loadOutcomes();
    }

    public AppointmentOutcomeSystem() {
        loadOutcomes();
    }

    public static List<AppointmentOutcomeRecord> getOutcomes() {
        return outcomes.stream()
                .filter(outcome -> !outcome.isDispensed()) // Filter out outcomes that are already dispensed
                .collect(Collectors.toList());
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
    public static AppointmentOutcomeRecord getOutcomeByAppointmentID(int appointmentID) {
        return outcomes.stream()
                .filter(outcome -> outcome.getAppointmentID() == appointmentID)
                .findFirst()
                .orElse(null);
    }

    /**
     * Loads all outcomes from the CSV file.
     */
    private static void loadOutcomes() {
        File file = new File(OUTCOMES_FILE);
        if (!file.exists()) {
            createOutcomesFile(file);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                parseAndAddOutcome(line);
            }
        } catch (Exception e) {
            System.err.println("Error loading outcomes: " + e.getMessage());
        }
    }

    /**
     * Creates an empty outcomes file with headers.
     *
     * @param file the file to create.
     */
    private static void createOutcomesFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes,Dispensed");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error creating outcomes file: " + e.getMessage());
        }
    }

    /**
     * Parses a CSV line and adds it to the outcomes list.
     */
    private static void parseAndAddOutcome(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return;

        int appointmentID = Integer.parseInt(parts[0]);
        String appointmentDate = parts[1];
        String serviceType = parts[2];
        String medicationsStr = parts[3];
        String consultationNotes = parts[4];
        boolean dispensed = Boolean.parseBoolean(parts[5]);

        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
                appointmentID, appointmentDate, serviceType, consultationNotes, dispensed
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

    /**
     * Saves all outcomes to the CSV file.
     */
    private static void saveOutcomes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTCOMES_FILE))) {
            bw.write("AppointmentID,Date,ServiceType,Medications,ConsultationNotes,Dispensed");
            bw.newLine();

            for (AppointmentOutcomeRecord outcome : outcomes) {
                bw.write(String.join(",",
                        String.valueOf(outcome.getAppointmentID()),
                        outcome.getAppointmentDate(),
                        outcome.getServiceType(),
                        outcome.getMedicationsAsString(),
                        outcome.getConsultationNotes(),
                        String.valueOf(outcome.isDispensed()) // Save the dispensed field
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving outcomes: " + e.getMessage());
        }
    }

    /**
     * Displays all outcomes for a doctor by doctor ID.
     *
     * @param doctorID the doctor's ID.
     */
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

        displayOutcomeTable(doctorOutcomes);
    }

    /**
     * Displays all outcomes for a patient by patient ID.
     *
     * @param patientID the patient's ID.
     */
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

        displayOutcomeTable(patientOutcomes);
    }

    /**
     * Displays all appointment outcomes.
     */
    public static void displayAllOutcomes() {
        if (outcomes.isEmpty()) {
            System.out.println("No outcomes recorded.");
            return;
        }

        displayOutcomeTable(outcomes);
    }

    /**
     * Displays outcomes in a tabular format.
     *
     * @param outcomesList the list of outcomes to display.
     */
    private static void displayOutcomeTable(List<AppointmentOutcomeRecord> outcomesList) {
        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+-------------------+");
        System.out.println("| AppointmentID | Date              | Service Type      | Medications                     | Consultation Notes     | Dispensed          |");
        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+-------------------+");

        for (AppointmentOutcomeRecord outcome : outcomesList) {
            System.out.printf("| %-13d | %-17s | %-17s | %-31s | %-21s | %-17s |\n",
                    outcome.getAppointmentID(),
                    outcome.getAppointmentDate(),
                    outcome.getServiceType(),
                    outcome.getMedicationsAsString(),
                    outcome.getConsultationNotes(),
                    outcome.isDispensed() ? "Yes" : "No");
        }

        System.out.println("+---------------+-------------------+-------------------+---------------------------------+-----------------------+-------------------+");
    }

    /**
     * Validates if the user input corresponds to a valid outcome ID.
     *
     * @param input the user input.
     * @return true if the input is a valid outcome ID, false otherwise.
     */
    public static boolean isValidOutcomeSelection(String input) {
        try {
            int outcomeID = Integer.parseInt(input);
            return outcomes.stream().anyMatch(outcome -> outcome.getAppointmentID() == outcomeID);
        } catch (NumberFormatException e) {
            return false;
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
                consultationNotes,
                false
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
            List<Stock> availableStocks = getStocks();
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

    private static boolean isValidStockSelection(String input, int size) {
        try {
            int index = Integer.parseInt(input);  // Try to parse the input to an integer
            return index > 0 && index <= size;   // Ensure the index is within the valid range (1 to size)
        } catch (NumberFormatException e) {
            return false; // If the input is not a valid number, return false
        }
    }

    public static void displayAllAppointmentOutcomes() {
        List<AppointmentOutcomeRecord> undispensedOutcomes = getOutcomes();


        if (undispensedOutcomes.isEmpty()) {
            System.out.println("No appointment outcomes available for dispensing.");
            return;
        }

        // Displaying the table header
        System.out.println("+-------------------+-------------------+-------------------+-----------------------+-------------------+");
        System.out.println("| Appointment ID    | Date              | Service Type      | Medications           | Dispensed          |");
        System.out.println("+-------------------+-------------------+-------------------+-----------------------+-------------------+");

        // Displaying each outcome in the table
        for (AppointmentOutcomeRecord outcome : outcomes) {
            System.out.printf("| %-17d | %-17s | %-17s | %-21s | %-17s |\n",
                    outcome.getAppointmentID(),
                    outcome.getAppointmentDate(),
                    outcome.getServiceType(),
                    outcome.getMedicationsAsString(),
                    outcome.isDispensed() ? "Yes" : "No");
        }

        System.out.println("+-------------------+-------------------+-------------------+-----------------------+-------------------+");
    }

    public static void dispenseMedication(int outcomeID) {
        AppointmentOutcomeRecord outcome = getOutcomeByAppointmentID(outcomeID);

        if (outcome == null) {
            System.out.println("Outcome record not found.");
            return;
        }

        // Step 1: Loop through each medication in the outcome and attempt to dispense
        for (Medication medication : outcome.getPrescribedMedications()) {
            Stock stock = findStockByMedicineName(medication.getMedicationName());

            if (stock == null) {
                System.out.println("Stock for medication " + medication.getMedicationName() + " not found.");
                continue;
            }

            if (stock.getStockLevel() < medication.getQuantity()) {
                System.out.println("Insufficient stock for " + medication.getMedicationName() + ". Available: "
                        + stock.getStockLevel() + ", Requested: " + medication.getQuantity());
                System.out.println("Dispensing stopped due to insufficient stock.");
                return; // Stop dispensing if not enough stock
            }

            // Step 2: Deduct the stock balance
            stock.setStockLevel(stock.getStockLevel() - medication.getQuantity());

            // Step 3: If stock is low, create a replenish request
            if (stock.getStockLevel() <= stock.getLowStockAlertThreshold()) {
                String replenishChoice = InputHandler.getValidatedInput(
                        "Stock for " + medication.getMedicationName() + " is low. Do you want to create a replenish request? (yes/no): ",
                        "Please enter 'yes' or 'no'.",
                        input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")
                );

                if (replenishChoice.equalsIgnoreCase("yes")) {
                    // Create replenish request
                    StockReplenishRequest replenishRequest = new StockReplenishRequest(
                            stock.getID(), 100, "pending" // Replenish with 100 units
                    );
                    new StockSystem().createReplenishRequest(replenishRequest);
                    System.out.println("Replenish request created for " + medication.getMedicationName() + ".");
                }
            }

            // Step 4: Mark the medication as dispensed
            medication.setStatus("dispensed");
            System.out.println("Dispensed " + medication.getQuantity() + " units of " + medication.getMedicationName());
        }

        // Step 5: Update the outcome to dispensed
        outcome.setDispensed(true);
        saveOutcomes();
        System.out.println("All medications dispensed and outcome updated.");
    }

    private static Stock findStockByMedicineName(String medicationName) {
        return getStocks()
                .stream()
                .filter(stock -> stock.getMedicineName().equalsIgnoreCase(medicationName))
                .findFirst()
                .orElse(null);
    }
}