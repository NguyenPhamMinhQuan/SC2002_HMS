package Users;

import Models.User;
import Systems.AppointmentSystem;
import Systems.InputHandler;
import Systems.StockSystem;
import Systems.UserManagementSystem;

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
        super(userId, password, "Administrator", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> manageUsers();
            case 2 -> AppointmentSystem.displayAllAppointments();
            case 3 -> StockSystem.printStocks();
            case 4 -> StockSystem.handleReplenishRequests();
            case 5 -> {
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

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
