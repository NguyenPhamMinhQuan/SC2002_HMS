package Models;

/**
 * Represents a user in the hospital management system.
 */
public class User {
    private final String userId; // User ID is final, as it should not change
    private String password;
    private final String role; // Role is final, as it should not change after creation
    private String name; // The name of the user
    private String gender; // Gender can be updated
    private int age; // Age can be updated

    /**
     * Constructs a new User.
     *
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param role     the role of the user (e.g., "Patient", "Doctor", etc.).
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user
     */
    public User(String userId, String password, String role, String name, String gender, int age) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    // Getters and Setters

    /**
     * Gets the user ID.
     *
     * @return the user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the user's role.
     *
     * @return the user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's name.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's gender.
     *
     * @return the user's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     *
     * @param gender the new gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the user's age.
     *
     * @return the user's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the user's age.
     *
     * @param age the new age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Validates the user's password.
     *
     * @param password the password to validate.
     * @return true if the password is correct; false otherwise.
     */
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Updates the user's password.
     *
     * @param newPassword the new password.
     */
    public void updatePassword(String newPassword) {
        setPassword(newPassword); // Use the setter for consistency
    }

    /**
     * Call the appropriate method from the user once a choice is given. Return logout boolean: true if user chose to log out
     *
     * @param feature the choice number on the user's menu
     */
    public boolean functionCall(int feature) {
        return false; // Default log-out value
    }
}
