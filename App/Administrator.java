
/**
 * Represents an administrator in the hospital management system.
 * Inherits from User class.
 */
public class Administrator extends User {

    /**
     * Constructs a new Administrator.
     * 
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user 
     */
    public Administrator( String userId, String password, String name, String gender, int age) {
        super( userId, password, "Administrator", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> {
                System.out.println("Viewing and managing hospital staff...");
                UserManagementSystem.filterStaff();
                
            }
            case 2 -> {
                System.out.println("Viewing appointment details...");

            }
            case 3 -> {
                System.out.println("Viewing and managing medication inventory...");
            }
            case 4 -> {
                System.out.println("Approving replenishment requests...");
            }
            case 5 -> {System.out.println("Logging out..."); return true;}
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }
}
