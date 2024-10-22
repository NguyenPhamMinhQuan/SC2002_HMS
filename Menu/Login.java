import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Login {

    public static User authenticate() {
        // Read users from file
        List<User> users = null;
        try {
            users = UserDB.readUsers("user.txt");
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
            return null; // Exit the login process if users can't be loaded
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Please log in.");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Check if the user exists and credentials match
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                scanner.close();
                return user;
            }
        }

        System.out.println("Invalid username or password. Try again.");
        scanner.close();
        return null;
    }
}
