import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        User loggedInUser = null;

        // Keep asking for login until successful
        while (loggedInUser == null) {
            loggedInUser = Login.authenticate();
        }

        // Display corresponding menu based on the user's role
        Menu userMenu;
        if (loggedInUser.getRole().equalsIgnoreCase("doctor")) {
            userMenu = new DoctorMenu();  // Polymorphism in action
        } else if (loggedInUser.getRole().equalsIgnoreCase("patient")) {
            userMenu = new PatientMenu();  // Polymorphism in action
        } else {
            System.out.println("Unknown role! Exiting...");
            return;
        }

        // Create scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Loop to display menu and handle user actions
        int choice = -1;
        while (choice != 9) { // 9 is assumed to be the logout option
            choice = userMenu.showMenuOptions();

            // Handle the user's choice
            switch (choice) {
                case 1:
                    // Call appropriate method, e.g., viewMedicalRecord()
                    System.out.println("Option 1 selected.");
                    break;
                case 2:
                    System.out.println("Option 2 selected.");
                    break;
                // Continue handling other cases based on the menu options
                // ...
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
