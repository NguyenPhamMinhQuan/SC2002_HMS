import java.util.List;
import java.util.Scanner;

/**
 * Hospital Management System (HMS) that manages user login, patient registration,
 * and user data persistence.
 */
public class HMS {

    private static final String USER_DATA_FILE = "users.txt";
    private UserManagementSystem userManagementSystem;
    private Scanner scanner;

    /**
     * Constructor for the HMS class, initializing the User Management System.
     */
    public HMS() {
        userManagementSystem = new UserManagementSystem();
    }

    /**
     * Load users from the data file into the User Management System.
     */
    public void loadUsers() {
        try {
            List<User> users = TextDB.loadObjects(USER_DATA_FILE, User.class);
            for (User user : users) {
                userManagementSystem.addUser(user);
            }
            System.out.println("Users loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Save users from the User Management System to the data file.
     */
    public void saveUsers() {
        try {
            List<User> users = userManagementSystem.getAllUsers();
            TextDB.saveObjects(USER_DATA_FILE, users);
            System.out.println("Users saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Start the Hospital Management System interface, allowing users to log in
     * or register as new patients.
     */
    public void start() {
        loadUsers(); // Load user data from the file.
        scanner = new Scanner(System.in);

        User user = null;

        System.out.println("Welcome to the Hospital Management System!");
        System.out.println("Please choose an option:");
        System.out.println("1. Log in");
        System.out.println("2. Register as a new patient");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character.

            if (choice == 1) {
                System.out.print("User ID: ");
                String userID = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                user = userManagementSystem.login(userID, password);
                if (user == null) {
                    System.out.println("Invalid User ID or Password. Please try again.");
                    return;
                }
            } else if (choice == 2) {
                System.out.println("Registering a new patient...");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Gender: ");
                String gender = scanner.nextLine();
                System.out.print("Age: ");
                int age = scanner.nextInt();

                user = userManagementSystem.createUser("patient", name, gender, age);
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
            case "patient" -> menu = new PatientMenu(user, scanner);
            case "doctor" -> menu = new DoctorMenu(user, scanner);
            case "pharmacist" -> menu = new PharmacistMenu(user, scanner);
            case "administrator" -> menu = new AdministratorMenu(user, scanner);
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
            saveUsers(); // Save user data back to the file.
            scanner.close();
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
