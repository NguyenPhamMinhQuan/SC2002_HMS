package Users;

import Enums.UserRole;
import Models.User;
import Systems.AppointmentOutcomeSystem;
import Systems.InputHandler;
import Systems.StockSystem;

import static Systems.AppointmentOutcomeSystem.isValidOutcomeSelection;


/**
 * Represents a pharmacist in the hospital management system.
 * Inherits from User class.
 */
public class Pharmacist extends User implements UserMenuInterface {
    /**
     * Constructs a new Pharmacist.
     *
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user
     */
    public Pharmacist(String userId, String password, String name, String gender, int age) {
        super(userId, password, UserRole.PHARMACIST, name, gender, age);
    }

    /**
     * Executes the specified feature from the pharmacist's menu.
     *
     * @param feature the feature option to execute.
     *                <ul>
     *                  <li>1 - View Appointment Outcome Record</li>
     *                  <li>2 - Update Prescription Status</li>
     *                  <li>3 - View Medication Inventory</li>
     *                  <li>4 - Submit Replenishment Request</li>
     *                  <li>5 - Logout</li>
     *                </ul>
     * @return {@code true} if pharmacist chooses to exit the menu; {@code false} otherwise.
     */
    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> AppointmentOutcomeSystem.displayAllOutcomes();
            case 2 -> handleDispensing();
            case 3 -> StockSystem.printStocks();
            case 4 -> StockSystem.showLowStockItemsAndCreateReplenishRequest();
            case 5 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Handles the process of dispensing medication based on the appointment outcome.
     * The pharmacist selects an appointment outcome from the available list and dispenses medication.
     * If no outcomes are available or the pharmacist cancels the process, the action is aborted.
     */
    private void handleDispensing() {
        if (AppointmentOutcomeSystem.getOutcomes().isEmpty()) {
            System.out.println("No appointment outcomes available for dispensing.");
            return;
        }

        AppointmentOutcomeSystem.displayAllAppointmentOutcomes();

        String selectedOutcomeID = InputHandler.getValidatedInput(
                "Enter the Appointment ID to dispense medications or type 'exit' to cancel: ",
                "Invalid input. Please enter a valid Appointment ID or 'exit'.",
                input -> input.equalsIgnoreCase("exit") || isValidOutcomeSelection(input)
        );

        if (selectedOutcomeID.equalsIgnoreCase("exit")) {
            System.out.println("Exiting pharmacist menu.");
            return;
        }

        int outcomeID = Integer.parseInt(selectedOutcomeID);
        AppointmentOutcomeSystem.dispenseMedication(outcomeID);
    }
}
