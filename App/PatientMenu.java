import java.util.Scanner;
/**
 * Represents the menu interface for patients.
 */
public class PatientMenu extends Menu {
    public PatientMenu(User user, Scanner scanner) {
        super(user, scanner);
    }

    @Override
    public int displayOptions() {
        while (true) {
            System.out.println("Patient Menu:");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            
            return choice;
        }
    }
}
