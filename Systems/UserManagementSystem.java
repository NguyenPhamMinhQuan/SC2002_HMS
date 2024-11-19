package Systems;

import Models.User;
import Users.Administrator;
import Users.Doctor;
import Users.Patient;
import Users.Pharmacist;

import java.io.*;
import java.util.*;

/**
 * Utility class to manage user authentication and role-specific access.
 * It includes methods to add, load, save users, and authenticate them.
 */
public class UserManagementSystem {
    private static final String USERS_FILE = "data/users.csv";

    public static Map<String, User> users = new HashMap<>();

    private static int patientCount = 0;
    private static int doctorCount = 0;
    private static int adminCount = 0;
    private static int pharmacistCount = 0;

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users.
     */
    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Retrieves all users with a specific role.
     *
     * @param role the role to filter by (e.g., "patient", "doctor").
     * @return a list of users with the specified role.
     */
    public static List<User> getUsersByRole(String role) {
        return getUsersByRoles(Collections.singleton(role));
    }

    /**
     * Retrieves all users matching any of the specified roles.
     *
     * @param roles a collection of roles to filter by.
     * @return a list of users matching the specified roles.
     */
    public static List<User> getUsersByRoles(Collection<String> roles) {
        return getAllUsers().stream()
                .filter(user -> roles.stream().anyMatch(role -> role.equalsIgnoreCase(user.getRole())))
                .toList();
    }

    /**
     * Displays a user in the formatted display table
     * @param user the user object
     */
    public static void displayUser(User user) {
        displayUserTable(Collections.singletonList(user));
    }

    /**
     * Displays all users with the "doctor" role in a table format.
     */
    public static void displayDoctors() {
        displayUserTable(getUsersByRole("doctor"));
    }


    /**
     * Displays all users with the "patient" role in a table format.
     */
    public static void displayPatients() {
        displayUserTable(getUsersByRole("patient"));
    }


    /**
     * Displays all users with "patient", "doctor", or "admin" roles in a table format.
     */
    public static void displayStaff() {
        displayUserTable(getUsersByRoles(Arrays.asList("patient", "doctor", "admin")));
    }


    /**
     * Helper method to display a list of users as a table in CLI.
     *
     * @param users the list of users to display.
     */
    private static void displayUserTable(List<User> users) {
        System.out.println("+------------+-------------------+---------------+-----------+");
        System.out.println("| User ID    | Name              | Role          | Gender    |");
        System.out.println("+------------+-------------------+---------------+-----------+");

        for (User user : users) {
            System.out.printf("| %-10s | %-17s | %-13s | %-9s |\n",
                    user.getUserId(),
                    user.getName(),
                    user.getRole(),
                    user.getGender());
        }

        System.out.println("+------------+-------------------+---------------+-----------+");
    }


    /**
     * Authenticates a user's login.
     * If the user is using the default password (their first time logging in), ask the user to change password
     *
     * @param userId   the user ID.
     * @param password the password.
     * @return the authenticated user if credentials are valid; otherwise, null.
     */
    public static User login(String userId, String password) {
        User user = users.get(userId);

        if (user != null && user.validatePassword(password)) {
            if (user.getPassword().equals("password")) {
                System.out.println("Please change your password (ie. Do not use the default password)");
                String newPassword = InputHandler.getValidatedInput(
                        "New Password:",
                        "Invalid password, do not use 'password'.",
                        input -> !input.trim().isEmpty() && !input.equals("password")
                );
                user.updatePassword(newPassword);
                saveUsers();
            }

            return user;
        }

        return null; // Invalid credentials
    }


    /**
     * Adds a new user to the system and increments the relevant role-specific counter.
     *
     * @param user the user to add.
     */
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        switch (user.getRole().toLowerCase()) {
            case "patient":
                patientCount++;
                break;
            case "doctor":
                doctorCount++;
                break;
            case "administrator":
                adminCount++;
                break;
            case "pharmacist":
                pharmacistCount++;
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + user.getRole());
        }
    }


    /**
     * Interactively adds a new patient.
     *
     * @return the created Patient object.
     */
    public static User addNewPatient() {
        return addNewUser("patient");
    }


    /**
     * Adds a new user to the system interactively.
     *
     * @param role (nullable)
     */
    public static User addNewUser(String role) {
        if (role == null) {
            role = InputHandler.getValidatedInput(
                    "Enter role (patient, doctor, pharmacist, administrator, or 'exit' to cancel): ",
                    "Invalid role. Please enter one of: patient, doctor, pharmacist, administrator.",
                    input -> input.toLowerCase().matches("(?i)patient|doctor|pharmacist|administrator")
            );
        }


        if (role == null) {
            System.out.println("Exiting...");
            return null;
        }

        String name = InputHandler.getValidatedInput(
                "Enter name (or 'exit' to cancel): ",
                "Invalid name. Name cannot be empty.",
                input -> !input.trim().isEmpty()
        );

        if (name == null) {
            System.out.println("Exiting...");
            return null;
        }

        String gender = InputHandler.getValidatedInput(
                "Enter gender (Male/Female or 'exit' to cancel): ",
                "Invalid gender. Please enter 'Male' or 'Female'.",
                input -> input.equalsIgnoreCase("male") || input.equalsIgnoreCase("female")
        );

        if (gender == null) {
            System.out.println("Exiting...");
            return null;
        }

        String ageStr = InputHandler.getValidatedInput(
                "Enter age (or 'exit' to cancel): ",
                "Invalid age. Please enter a positive number.",
                input -> {
                    try {
                        int age = Integer.parseInt(input);
                        return age > 0 && age <= 100;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        if (ageStr == null) {
            System.out.println("Exiting...");
            return null;
        }

        int age = Integer.parseInt(ageStr);

        User user = createUser(role, name, gender.toLowerCase(), age);
        saveUsers();

        System.out.println("User added successfully.\n" + user);
        displayUser(user);

        return user;
    }


    /**
     * Updates an existing user's details.
     */
    public static void updateUser() {

        System.out.println("Enter the User ID of the user to update (or type 'exit' to cancel): ");
        String userId = InputHandler.getValidatedInput(
                "Enter the User ID of the user to update (or 'exit' to cancel): ",
                "Invalid User ID. Please try again.",
                input -> !input.trim().isEmpty()
        );

        if (userId == null || userId.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            return;
        }

        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        // Update name
        String name = InputHandler.getValidatedInput(
                "Enter new name (leave blank to keep unchanged, or 'exit' to cancel): ",
                "Invalid name. Please try again.",
                input -> true // Allow blank input to skip updating the name
        );
        if (name == null || name.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            return;
        }
        if (!name.trim().isEmpty()) {
            user.setName(name.trim());
        }

        // Update gender
        String gender = InputHandler.getValidatedInput(
                "Enter new gender (Male/Female, leave blank to keep unchanged, or 'exit' to cancel): ",
                "Invalid gender. Please enter 'Male' or 'Female'.",
                input -> input.isEmpty() || input.equalsIgnoreCase("male") || input.equalsIgnoreCase("female")
        );
        if (gender == null || gender.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            return;
        }
        if (!gender.trim().isEmpty()) {
            user.setGender(gender.trim());
        }

        // Update age
        String ageInput = InputHandler.getValidatedInput(
                "Enter new age (or leave blank to keep unchanged, or 'exit' to cancel): ",
                "Invalid age. Please enter a positive number.",
                input -> {
                    try {
                        return input.isEmpty() || Integer.parseInt(input) > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );
        if (ageInput == null || ageInput.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            return;
        }
        if (!ageInput.trim().isEmpty()) {
            user.setAge(Integer.parseInt(ageInput.trim()));
        }

        System.out.println("User updated successfully: " + user);
        saveUsers(); // Save users to file after updating
    }


    /**
     * Deletes a user from the system.
     */
    public static void deleteUser() {
        String userId = InputHandler.getValidatedInput(
                "Enter the User ID of the user you want to delete or 'exit' to cancel: ",
                "Please select the correct user ID",
                input -> users.get(input) == null);

        User user = users.remove(userId);

        switch (user.getRole().toLowerCase()) {
            case "patient":
                patientCount--;
                break;
            case "doctor":
                doctorCount--;
                break;
            case "administrator":
                adminCount--;
                break;
            case "pharmacist":
                pharmacistCount--;
                break;
        }

        System.out.println("User deleted successfully: " + user);
        saveUsers(); // Save users to file after deleting
    }


    /**
     * Creates a user based on their role and registers them in the system with a default password of "password"
     *
     * @param role   the role of the user (e.g., "patient", "doctor").
     * @param name   the name of the user.
     * @param gender the gender of the user (Male or Female).
     * @param age    the age of the user.
     * @return the created User object.
     */
    public static User createUser(String role, String name, String gender, int age) {
        User user = switch (role.toLowerCase()) {
            case "patient" -> new Patient("PT" + (++patientCount), "password", name, gender, age);
            case "doctor" -> new Doctor("D" + (++doctorCount), "password", name, gender, age);
            case "pharmacist" -> new Pharmacist("P" + (++pharmacistCount), "password", name, gender, age);
            case "administrator" -> new Administrator("A" + (++adminCount), "password", name, gender, age);
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
        addUser(user);
        return user;
    }


    /**
     * Loads users from the saved file.
     * This method will read the users from the CSV and populate the users map.
     */
    public static void loadUsers() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
        String line;
        br.readLine(); // Skip Header

        while ((line = br.readLine()) != null) {
            String[] userDetails = line.split(",");
            String userId = userDetails[0];
            String password = userDetails[1];
            String name = userDetails[2];
            String gender = userDetails[3];
            int age = Integer.parseInt(userDetails[4]);
            String role = userDetails[5];

            User user = switch (role.toLowerCase()) {
                case "patient" -> new Patient(userId, password, name, gender, age);
                case "doctor" -> new Doctor(userId, password, name, gender, age);
                case "pharmacist" -> new Pharmacist(userId, password, name, gender, age);
                case "administrator" -> new Administrator(userId, password, name, gender, age);
                default -> null;
            };
            addUser(user);
        }
        br.close();
    }


    /**
     * Saves all users to the file.
     * This method writes all user data to the CSV file for persistence.
     */
    public static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            bw.write("UserID,Password,Name,Gender,Age,Role");
            bw.newLine();

            for (User user : users.values()) {
                String userLine = String.join(",",
                        user.getUserId(),
                        user.getPassword(),
                        user.getName(),
                        user.getGender(),
                        String.valueOf(user.getAge()),
                        user.getRole().toLowerCase()
                );
                bw.write(userLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }
}
