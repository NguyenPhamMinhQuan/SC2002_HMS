import java.util.Scanner;

public class DoctorMenu implements Menu {
    @Override
    public int showMenuOptions() {
        System.out.println("Doctor Menu:");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");

        Scanner sc = new Scanner(System.in);
        int choice = -1;  // Declare and initialize choice outside the loop
        while (true) {
            try {
                System.out.print("Your choice: ");
                choice = sc.nextInt();
                System.out.println("----------------------");
                if (choice >= 1 && choice <= 8) {
                    break; // Valid input, break the loop
                } else {
                    System.out.println("Invalid input, please input a number from 1 to 8.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please input a number from 1 to 8.");
                sc.next(); // Consume invalid input
            }
        }
        
        sc.close(); // Close the scanner after use

        if (choice == 8) {
            // Handle logout logic if needed
            System.out.println("Logging out...");
        }
        
        return choice;
    }
}
