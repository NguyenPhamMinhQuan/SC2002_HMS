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
    public static Map<String, User> users = new HashMap<>();
    private static int patientCount = 0;
    private static int doctorCount = 0;
    private static int adminCount = 0;
    private static int pharmacistCount = 0;
    private static final String USERS_FILE = "data/users.csv";

    /**
     * Adds a new user to the system and increments the relevant role-specific counter.
     *
     * @param user the user to add.
     */
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        // Increment the counter based on the user's role
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
     * Adds a new user to the system interactively.
     */
    public static void addNewUser() {
        System.out.println("Enter role (patient, doctor, pharmacist, administrator): ");
        String role = InputHandler.nextLine();

        System.out.println("Enter name: ");
        String name = InputHandler.nextLine();

        System.out.println("Enter gender (Male/Female): ");
        String gender = InputHandler.nextLine();

        System.out.println("Enter age: ");
        int age = Integer.parseInt(InputHandler.nextLine());

        User user = createUser(role, name, gender, age);
        System.out.println("User added successfully: " + user);

        saveUsers(); // Save users to file after adding
    }


    /**
     * Updates an existing user's details.
     */
    public static void updateUser() {
        System.out.println("Enter the User ID of the user to update: ");
        String userId = InputHandler.nextLine();

        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Enter new name (leave blank to keep unchanged): ");
        String name = InputHandler.nextLine();
        if (!name.isEmpty()) user.setName(name);

        System.out.println("Enter new gender (leave blank to keep unchanged): ");
        String gender = InputHandler.nextLine();
        if (!gender.isEmpty()) user.setGender(gender);

        System.out.println("Enter new age (or press Enter to keep unchanged): ");
        String ageInput = InputHandler.nextLine();
        if (!ageInput.isEmpty()) user.setAge(Integer.parseInt(ageInput));

        System.out.println("User updated successfully: " + user);

        saveUsers(); // Save users to file after updating
    }

    /**
     * Deletes a user from the system.
     */
    public static void deleteUser() {
        System.out.println("Enter the User ID of the user to delete: ");
        String userId = InputHandler.nextLine();

        User user = users.remove(userId);
        if (user == null) {
            System.out.println("User not found.");
        } else {
            System.out.println("User deleted successfully: " + user);
            // Decrement the relevant counter
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

            saveUsers(); // Save users to file after deleting
        }
    }


    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users.
     */
    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values()); // Convert the values of the HashMap to a List
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
            if (user.getPassword() == "password") {
                System.out.println("Please change your password (ie. Don't use the default password)");
                System.out.print("New Password:");
                String newPassword = InputHandler.nextLine();
                changePassword(user, "password", newPassword);
            }
            return user;
        }
        return null; // Invalid credentials
    }

    /**
     * Allows a user to change their password after login.
     *
     * @param user        the user who wants to change their password.
     * @param oldPassword the old password.
     * @param newPassword the new password.
     * @throws SecurityException if the old password is incorrect.
     */
    public static void changePassword(User user, String oldPassword, String newPassword) {
        if (!user.validatePassword(oldPassword)) {
            throw new SecurityException("Incorrect current password.");
        }
        user.updatePassword(newPassword);
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
        User user;
        switch (role.toLowerCase()) {
            case "patient":
                user = new Patient("" + (++patientCount), "password", name, gender, age);
                break;
            case "doctor":
                user = new Doctor("D" + (++doctorCount), "password", name, gender, age);
                break;
            case "pharmacist":
                user = new Pharmacist("P" + (++pharmacistCount), "password", name, gender, age);
                break;
            case "administrator":
                user = new Administrator("A" + (++adminCount), "password", name, gender, age);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        addUser(user); // Register the user in the system
        return user;
    }

    /**
     * Filters users to return only patients.
     * Displays the filtered patients as tables on the CLI.
     */
    public static void filterPatients() {
        List<User> patients = new ArrayList<>();


        // Filter users into patients and staff
        for (User user : users.values()) {
            if (user.getRole().equalsIgnoreCase("patient")) {
                patients.add(user);
            }
        }

        // Display patients table
        System.out.println("Patients:");
        displayUserTable(patients);
    }

    /**
     * Filters users to return only staff (doctors, pharmacists, and administrators).
     * Displays the filtered staff as tables on the CLI.
     */
    public static void filterStaff() {
        List<User> staff = new ArrayList<>();


        // Filter users into patients and staff
        for (User user : users.values()) {
            if (user.getRole().equalsIgnoreCase("doctor") ||
                    user.getRole().equalsIgnoreCase("pharmacist") ||
                    user.getRole().equalsIgnoreCase("administrator")) {
                staff.add(user);
            }
        }

        // Display staff table
        System.out.println("Staff:");
        displayUserTable(staff);
    }

    public static void filterDoctor() {
        List<User> staff = new ArrayList<>();


        // Filter users into patients and staff
        for (User user : users.values()) {
            if (user.getRole().equalsIgnoreCase("doctor"))
                staff.add(user);
        }

        displayUserTable(staff);
    }

    /**
     * Helper method to display a list of users as a table in CLI.
     *
     * @param users the list of users to display.
     */
    private static void displayUserTable(List<User> users) {
        // Print table header
        System.out.println("+------------+-------------------+---------------+-----------+");
        System.out.println("| User ID    | Name              | Role          | Gender    |");
        System.out.println("+------------+-------------------+---------------+-----------+");

        // Print user details in table format
        for (User user : users) {
            System.out.printf("| %-10s | %-17s | %-9s | %-9s |\n",
                    user.getUserId(),
                    user.getName(),
                    user.getRole(),
                    user.getGender());
        }

        // Print table footer
        System.out.println("+------------+-------------------+-----------+-----------+");
    }

    /**
     * Loads users from the saved file.
     * This method will read the users from the CSV and populate the users map.
     */
    public static void loadUsers() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            String[] userDetails = line.split(",");
            String userId = userDetails[0];
            String password = userDetails[1];
            String name = userDetails[2];
            String gender = userDetails[3];
            int age = Integer.parseInt(userDetails[4]);
            String role = userDetails[5];

            // Create the user based on the role
            User user = null;
            switch (role.toLowerCase()) {
                case "patient":
                    user = new Patient(userId, password, name, gender, age);
                    break;
                case "doctor":
                    user = new Doctor(userId, password, name, gender, age);
                    break;
                case "pharmacist":
                    user = new Pharmacist(userId, password, name, gender, age);
                    break;
                case "administrator":
                    user = new Administrator(userId, password, name, gender, age);
                    break;
            }
            addUser(user);
        }
        br.close();
    }

    /**
     * Saves all users to the file.
     * This method writes all user data to the CSV file for persistence.
     */
    public static void saveUsers() {
        File file = new File(USERS_FILE);

        // Ensure the directory exists
        file.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Write a header row for clarity
            bw.write("UserID,Password,Name,Gender,Age,Role");
            bw.newLine();

            // Write user data
            for (User user : users.values()) {
                String userLine = String.join(",",
                        user.getUserId(),
                        user.getPassword(),
                        user.getName(),
                        user.getGender(),
                        String.valueOf(user.getAge()),
                        user.getRole()
                );
                bw.write(userLine);
                bw.newLine();
            }

            System.out.println("Users saved successfully to " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
