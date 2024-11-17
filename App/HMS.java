import java.io.IOException;



/**
 * Hospital Management System (HMS) that manages user login, patient registration,
 * and user data persistence.
 */
public class HMS {

    public HMS() {}

    /**
     * Start the Hospital Management System interface, allowing users to log in
     * or register as new patients.
     */
    public void start() {
        try {
            UserManagementSystem.loadUsers(); // Load user data from the file.
            System.out.println("Users loaded successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while loading users: " + e.getMessage());
        }

        User user = null;

        System.out.println("Welcome to the Hospital Management System!");
        System.out.println("Please choose an option:");
        System.out.println("1. Log in");
        System.out.println("2. Register as a new patient");

        try {
            int choice = InputHandler.nextInt();

            if (choice == 1) {
                System.out.print("User ID: ");
                String userID = InputHandler.nextLine();
                System.out.print("Password: ");
                String password = InputHandler.nextLine();

                user = UserManagementSystem.login(userID, password);
                if (user == null) {
                    System.out.println("Invalid User ID or Password. Please try again.");
                    return;
                }
            } else if (choice == 2) {
                System.out.println("Registering a new patient...");
                System.out.print("Name: ");
                String name = InputHandler.nextLine();
                System.out.print("Gender: ");
                String gender = InputHandler.nextLine();
                System.out.print("Age: ");
                int age = InputHandler.nextInt();

                user = UserManagementSystem.createUser("patient", name, gender, age);
                System.out.println("Patient registered successfully! Your User ID is: " + user.getUserId());
                System.out.println("The default password is 'password'");
            } else {
                System.out.println("Invalid choice. Please restart the application.");
                return;
            }

            System.out.println("Welcome, " + user.getRole() + " " + user.getName() + ".");
            System.out.println("How can I help you today?");
            Menu menu;
            switch (user.getRole().toLowerCase()) {
            case "patient" -> menu = new PatientMenu(user);
            case "doctor" -> menu = new DoctorMenu(user);
            case "pharmacist" -> menu = new PharmacistMenu(user);
            case "administrator" -> menu = new AdministratorMenu(user);
            default -> {
                System.out.println("Invalid role. Exiting...");
                return;
                }
            }
            
            int menuChoice;
            boolean logout;
            do {
                menuChoice = menu.displayOptions();
                logout = user.functionCall(menuChoice);
            } while (!logout);

        } catch (Exception e) {
            System.out.println("An error occurred. Please restart the application.");
        } finally {
            try {
                UserManagementSystem.saveUsers(); // save user data from the file.
                System.out.println("Users saved successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while saving users: " + e.getMessage());
            }
    
            InputHandler.close();
        }
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
