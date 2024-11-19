package Users;

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
        super(userId, password, "Pharmacist", name, gender, age);
    }

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

    private void handleDispensing() {
        AppointmentOutcomeSystem.displayAllAppointmentOutcomes();

        // Step 2: Ask the pharmacist to select an outcome to dispense medication
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
