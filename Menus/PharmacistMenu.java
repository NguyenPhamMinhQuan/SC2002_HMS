package Menus;

import Models.User;
import Systems.InputHandler;

/**
 * Represents the menu interface for pharmacists.
 */
public class PharmacistMenu extends Menu {

    /**
     * Constructs a PharmacistMenu for the given user.
     *
     * @param user the logged-in pharmacist user.
     */
    public PharmacistMenu(User user) {
        super(user);
    }

    /**
     * Displays menu options for the pharmacist and returns the user's choice.
     *
     * @return the option selected by the user.
     */
    @Override
    public int displayOptions() {
        System.out.println("Pharmacist Menu:");
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        return InputHandler.nextInt();
    }
}
