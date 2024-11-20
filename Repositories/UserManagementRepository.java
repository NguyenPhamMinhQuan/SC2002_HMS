package HMS.Repositories;

import HMS.Enums.UserRole;
import HMS.Models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementRepository implements LoadandSaveInterface<User> {

    private static final String USERS_FILE = "data/users.csv";

    @Override
    public List<User> loadData() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String userID = details[0];
                String password = details[1];
                String name = details[2];
                String gender = details[3];
                int age = Integer.parseInt(details[4]);
                String role = details[5];
                // Convert the role string to the UserRole enum
                UserRole roleEnum = UserRole.valueOf(role.toUpperCase());

                // Now create the User object using the roleEnum
                User user = new User(userID, password, roleEnum, name, gender, age);
                users.add(user);

            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void saveData(List<User> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            bw.write("UserID,Password,Name,Gender,Age,Role");
            bw.newLine();
            for (User user : data) {
                bw.write(String.join(",",
                        user.getUserId(),
                        user.getPassword(),
                        user.getName(),
                        user.getGender(),
                        String.valueOf(user.getAge()),
                        user.getRole().toString() // Convert UserRole enum to String
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
}
