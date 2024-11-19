package Menus;

import Models.User;
import Systems.InputHandler;

/**
 * Represents the menu interface for administrators.
 */
public class AdministratorMenu extends Menu {

    public AdministratorMenu(User user) {
        super(user);
    }

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
