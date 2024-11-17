/**
 * Represents a pharmacist in the hospital management system.
 * Inherits from User class.
 */
public class Pharmacist extends User {

    /**
     * Constructs a new Pharmacist.
     * 
     * @param userId   the unique identifier of the user.
     * @param password the password of the user.
     * @param name     the name of the user.
     * @param gender   the gender of the user (Male or Female)
     * @param age      the age of the user 
     */
    public Pharmacist(String userId, String password, String name, String gender, int age) {
        super(userId, password, "Pharmacist", name, gender, age);
    }

    @Override
    public boolean functionCall(int feature) {
        switch (feature) {
            case 1 -> {
                System.out.println("Viewing appointment outcome record...");
            }
            case 2 -> {
                System.out.println("Updating prescription status...");
            }
            case 3 -> {
                System.out.println("Viewing medication inventory...");
            }
            case 4 -> {
                System.out.println("Submitting replenishment request...");
            }
            case 5 -> {System.out.println("Logging out...");return true;}
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }
}
