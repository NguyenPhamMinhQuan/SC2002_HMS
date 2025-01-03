package HMS.Users;

import HMS.Enums.UserRole;
import HMS.Models.User;
import HMS.Systems.AppointmentSystem;
import HMS.Systems.InputHandler;
import HMS.Systems.StockSystem;
import HMS.Systems.UserManagementSystem;

/**
 * Represents an administrator in the hospital management system.
 * Inherits from User class.
 */
public class Administrator extends User implements UserMenuInterface {

    /**
     * Constructs a new Administrator.
     *
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user
     */
    public Administrator(String userId, String password, String name, String gender, int age) {
        super(userId, password, UserRole.ADMINISTRATOR, name, gender, age);
    }


    /**
     * Processes the selected feature from the Administrator's menu.
     *
     * @param feature the feature option to execute.
     *                <ul>
     *                  <li>0 - Change password</li>
     *                  <li>1 - View and Manage Hospital Staff</li>
     *                  <li>2 - Display all appointments</li>
     *                  <li>3 - View and Mange stock inventory</li>
     *                  <li>4 - Handle stock replenishment requests</li>
     *                  <li>5 - Exit the menu</li>
     *                </ul>
     * @return {@code true} if the administrator chooses to exit the menu, otherwise {@code false}.
     */
    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 0 -> UserManagementSystem.updatePassword(getUserId());
            case 1 -> manageUsers();
            case 2 -> AppointmentSystem.displayAllAppointments();
            case 3 -> {
                StockSystem.printStocks();
                StockSystem.showLowStockItemsAndCreateReplenishRequest(); //Keep a log of all the replenishment and only allow for replenishing stocks with low level
                StockSystem.handleReplenishRequests();
                StockSystem.printStocks();
            }
            case 4 -> StockSystem.handleReplenishRequests();
            case 5 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Provides a menu to manage hospital staff, including adding, updating, and deleting users.
     * The administrator can:
     * <ul>
     *   <li>View the list of staff.</li>
     *   <li>Choose to add, update, or delete a user.</li>
     *   <li>Return to the main menu if no action is taken.</li>
     * </ul>
     */
    private void manageUsers() {
        System.out.println("\n--- Viewing and Managing Hospital Staff ---");
        UserManagementSystem.displayStaff();

        System.out.println("Choose an action:");
        System.out.println("1. Add a user");
        System.out.println("2. Update a user");
        System.out.println("3. Delete a user");
        System.out.println("4. Back to main menu");

        String action = InputHandler.getValidatedInputWithExit(
                "Choose an action (1-4) or type 'exit' to go back: ",
                "Invalid input. Please choose a value from 1 to 4 or type 'exit'.",
                input -> input.matches("[1-4]")
        );

        if (action == null) {
            System.out.println("Returning to main menu...");
            return;
        }

        switch (action) {
            case "1" ->
                UserManagementSystem.addNewUserMenu(null);
            case "2" -> UserManagementSystem.updateUser();
            case "3" -> UserManagementSystem.deleteUser();
            case "4" -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice. Returning to main menu...");
        }
    }
}
