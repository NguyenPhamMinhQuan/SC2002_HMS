package Menus;

import Models.User;
import Systems.InputHandler;

/**
 * Represents the menu interface for administrators.
 */
public class AdministratorMenu extends Menu {

    /**
     * Constructs a new AdministratorMenu for the given user.
     *
     * @param user the user object representing the administrator who is logging in
     */
    public AdministratorMenu(User user) {
        super(user);
    }

    /**
     * Displays the available options in the Administrator's menu.
     * The user can select an option by entering the corresponding number.
     *
     * @return the number corresponding to the selected option
     */
    @Override
    public int displayOptions() {
        System.out.println("Administrator Menu:");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointments Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        return InputHandler.nextInt();
    }
}
