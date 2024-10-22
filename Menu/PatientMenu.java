import java.util.Scanner;

public class PatientMenu implements Menu {
    @Override
    public int showMenuOptions() {
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

        Scanner sc = new Scanner(System.in);
        int choice = -1;  // Declare and initialize choice outside the loop
        while (true) {
            try {
                System.out.print("Your choice: ");
                choice = sc.nextInt();
                System.out.println("----------------------");
                if (choice >= 1 && choice <= 9) {
                    break; // Valid input, break the loop
                } else {
                    System.out.println("Invalid input, please input a number from 1 to 9.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please input a number from 1 to 9.");
                sc.next(); // Consume invalid input
            }
        }
        
        sc.close(); // Close the scanner after use

        if (choice == 9) {
            // Handle logout logic if needed
            System.out.println("Logging out...");
        }
        
        return choice;
    }
}
