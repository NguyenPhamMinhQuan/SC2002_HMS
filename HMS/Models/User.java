package HMS.Models;

import HMS.Enums.UserRole;
import HMS.Systems.UserManagementSystem;

/**
 * Represents a user in the hospital management system.
 * This class holds user details such as ID, role, name, gender, and age.
 * It provides methods to access and modify certain user information.
 */
public class User {
    private final String userId; // User ID is final, as it should not change
    private final UserRole role; // Role is final, as it should not change after creation
    private String password;
    private String name;
    private String gender;
    private int age;

    /**
     * Constructs a new user object with the specified details.
     *
     * @param userId    The unique ID for each user.
     * @param password  The user's password.
     * @param role      The role of the user in the HMS.HMS (Patient, Doctor, Pharmacist, Administrator)
     * @param name      The user's name.
     * @param gender    The user's gender.
     * @param age       The user's age.
     */
    public User(String userId, String password, UserRole role, String name, String gender, int age) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Gets the Unique ID of the user.
     *
     * @return The user's ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the role of the user.
     *
     * @return The user's role.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Gets the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the role of the user.
     *
     * @param name The user's new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the gender of the user.
     *
     * @return The user's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the role of the user.
     *
     * @param gender The user's gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the age of the user.
     *
     * @return The user's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the user.
     *
     * @param age The user's age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Updates the user's password.
     *
     * @param newPassword the new password.
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
    
    /**
     * Placeholder method for functionality based on user menu choice.
     *
     * @param feature the choice number on the user's menu.
     * @return false for logout by default.
     */
    public boolean functionCall(int feature) {
        return false; // Default log-out value
    }
}