package Users;

import Models.User;
import Systems.AppointmentSystem;
import Systems.StockSystem;
import Systems.UserManagementSystem;

/**
 * Represents an administrator in the hospital management system.
 * Inherits from User class.
 */
public class Administrator extends User {

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
            case 1 -> {
                System.out.println("Viewing and managing hospital staff...");
                UserManagementSystem.filterStaff();
            }
            case 2 -> {
                System.out.println("Viewing appointment details...");
                AppointmentSystem.displayAppointments();
            }
            case 3 -> {
                System.out.println("Viewing and managing medication inventory...");
                manageMedicationInventory();
            }
            case 4 -> {
                handleReplenishRequests();
            }
            case 5 -> {
                System.out.println("Logging out...");
                return true;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Allows the administrator to view and manage medication inventory.
     */
    private void manageMedicationInventory() {
        StockSystem stockSystem = new StockSystem();
        stockSystem.printStocks();
    }

    /**
     * Allows the administrator to handle replenish requests interactively.
     */
    private void handleReplenishRequests() {
        StockSystem stockSystem = new StockSystem();
        stockSystem.handleReplenishRequests();
    }
}
