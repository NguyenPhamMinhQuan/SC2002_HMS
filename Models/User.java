package Models;

import Enums.UserRole;

/**
 * Represents a user in the hospital management system.
 */
public class User {
    private final String userId; // User ID is final, as it should not change
    private final UserRole role; // Role is final, as it should not change after creation
    private String password;
    private String name;
    private String gender;
    private int age;

    public User(String userId, String password, UserRole role, String name, String gender, int age) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

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