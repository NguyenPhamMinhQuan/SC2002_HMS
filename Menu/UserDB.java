import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UserDB {
    public static final String SEPARATOR = "|";

    // Example of reading user data from a file
    public static List<User> readUsers(String filename) throws IOException {
        // Read the lines from the file
        List<String> stringArray = read(filename);
        List<User> userList = new ArrayList<>(); // To store User objects

        // Parse each line and convert to User object
        for (String st : stringArray) {
            StringTokenizer tokenizer = new StringTokenizer(st, SEPARATOR);

            // Extract user data
            String username = tokenizer.nextToken().trim();
            String password = tokenizer.nextToken().trim();
            String role = tokenizer.nextToken().trim();

            // Create User object and add to list
            User user = new User(username, password, role);
            userList.add(user);
        }
        return userList;
    }

    // Example of saving user data to a file
    public static void saveUsers(String filename, List<User> userList) throws IOException {
        List<String> dataToWrite = new ArrayList<>();

        // Convert User objects to String
        for (User user : userList) {
            StringBuilder sb = new StringBuilder();
            sb.append(user.getUsername().trim());
            sb.append(SEPARATOR);
            sb.append(user.getPassword().trim());
            sb.append(SEPARATOR);
            sb.append(user.getRole().trim());
            dataToWrite.add(sb.toString());
        }

        // Write data to file
        write(filename, dataToWrite);
    }

    /** Write the content to the given file. */
    public static void write(String fileName, List<String> data) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (String record : data) {
                out.println(record);
            }
        }
    }

    /** Read the contents of the given file. */
    public static List<String> read(String fileName) throws IOException {
        List<String> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        }
        return data;
    }

    public static void main(String[] args) {
        String filename = "user.txt";
        try {
            // Read user records from the file
            List<User> users = UserDB.readUsers(filename);
            for (User user : users) {
                System.out.println("Name: " + user.getUsername());
                System.out.println("Role: " + user.getRole());
            }

            // Add a new user and save to file
            User newUser = new User("Joseph", "XXXXXXX", "Doctor");
            users.add(newUser);
            UserDB.saveUsers(filename, users);

        } catch (IOException e) {
            System.out.println("IOException > " + e.getMessage());
        }
    }
}
