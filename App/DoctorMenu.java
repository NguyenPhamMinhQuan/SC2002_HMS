import java.util.Scanner;
/**
 * Represents the menu interface for doctors.
 */
public class DoctorMenu extends Menu {
    public DoctorMenu(User user, Scanner scanner) {
        super(user, scanner);
    }

    @Override
    public int displayOptions() {
        while (true) {
            System.out.println("Doctor Menu:");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            return choice;
        }
    }
}
