package HMS;

import HMS.Enums.UserRole;
import HMS.Menus.*;
import HMS.Models.User;
import HMS.Systems.InputHandler;
import HMS.Systems.UserManagementSystem;

import java.io.IOException;

/**
 * Hospital Management System (HMS) that manages user login, patient registration,
 * and user HMS.data persistence.
 */
public class HMS {

    public HMS() {}

    /**
     * Start the Hospital Management System interface, allowing users to log in
     * or register as new patients.
     */
    public void start() {
        try {
            UserManagementSystem.loadUsers(); // Load user HMS.data from the file.
            System.out.println("Users loaded successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while loading users: " + e.getMessage());
        }

        System.out.println("Welcome to the Hospital Management System!\n");

        while (true) { // Main loop for login and registration
            System.out.println("Please choose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Register as a new patient");
            System.out.println("3. Exit");

            try {
                int choice = InputHandler.nextInt();

                if (choice == 3) {
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                }

                User user = null;

                if (choice == 1) {
                    while (user == null) { // Loop until login is successful or user cancels
                        System.out.print("User ID: ");
                        String userID = InputHandler.nextLine();
                        System.out.print("Password: ");
                        String password = InputHandler.nextLine();

                        user = UserManagementSystem.login(userID, password);

                        if (user == null) {
                            System.out.println("Invalid User ID or Password. 'enter' to try again or 'exit' to exit.");
                            String retry = InputHandler.nextLine();

                            if (retry.equalsIgnoreCase("exit")) {
                                break;
                            }
                        }
                    }
                } else if (choice == 2) {
                    user = UserManagementSystem.addNewUserMenu("patient");
                    System.out.println("Patient registered successfully! Your User ID is: " + user.getUserId());
                    System.out.println("The default password is 'password'.");
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                if (user != null) {
                    /*
                      This logic handles logged-in users
                     */
                    System.out.println("\nWelcome, " + user.getRole() + " " + user.getName() + ".");

                    Menu menu = switch (user.getRole()) {
                        case UserRole.PATIENT -> new PatientMenu(user);
                        case UserRole.DOCTOR -> new DoctorMenu(user);
                        case UserRole.PHARMACIST -> new PharmacistMenu(user);
                        case UserRole.ADMINISTRATOR -> new AdministratorMenu(user);
                    };

                    boolean logout = false;
                    while (!logout) { // Loop for menu until logout
                        System.out.println();
                        int menuChoice = menu.displayOptions();
                        System.out.println();

                        logout = user.functionCall(menuChoice);
                        if (logout) {
                            System.out.println("Logging out...");
                            System.out.println();
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("An error occurred. Please restart the application.");
                break;
            }
        }

        InputHandler.close();
    }

    /**
     * The entry point of the application, starting the HMS system.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        HMS hms = new HMS();
        hms.start();
    }
}
