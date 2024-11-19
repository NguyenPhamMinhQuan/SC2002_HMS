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
        System.out.println("Viewing and managing hospital staff...");
        UserManagementSystem.displayStaff();

        System.out.println("Choose an action:");
        System.out.println("1. Add a user");
        System.out.println("2. Update a user");
        System.out.println("3. Delete a user");
        System.out.println("4. Back to main menu");

        int action = Integer.parseInt(InputHandler.nextLine());
        switch (action) {
            case 1 -> UserManagementSystem.addNewUser(null);
            case 2 -> UserManagementSystem.updateUser();
            case 3 -> UserManagementSystem.deleteUser();
            case 4 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice. Returning to main menu...");
        }
    }


}
