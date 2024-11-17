import java.util.Scanner;
/**
 * Represents the menu interface for pharmacists.
 */
public class PharmacistMenu extends Menu {

    public PharmacistMenu(User user, Scanner scanner) {
        super(user, scanner);
    }

    @Override
    public int displayOptions() {
        while (true) {
            System.out.println("Pharmacist Menu:");
            System.out.println("1. View Appointment Outcome Record");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

           
            return choice;
        }
    }
}
