import java.util.Scanner;

public class AdminMenu {
    private AdminSystem adminSystem;
    private StaffManagement staffManagement;
    private Scanner scanner;

    // Constructor
    public AdminMenu(AdminSystem adminSystem, StaffManagement staffManagement) {
        this.adminSystem = adminSystem;
        this.staffManagement = staffManagement;
        this.scanner = new Scanner(System.in);
    }

    // Display the admin menu and handle user choices
    public void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> manageHospitalStaff();
                case 2 -> viewAppointmentsDetails();
                case 3 -> manageMedicationInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Option 1: View and Manage Hospital Staff
    private void manageHospitalStaff() {
        System.out.println("\n--- Manage Hospital Staff ---");
        System.out.println("1. View Staff List");
        System.out.println("2. Add Staff");
        System.out.println("3. Update Staff");
        System.out.println("4. Remove Staff");
        System.out.println("5. Filter Staff by Role, Gender, or Age");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> staffManagement.displayFilteredStaff(null, null, 0, 100); // View all staff
            case 2 -> addStaff();
            case 3 -> updateStaff();
            case 4 -> removeStaff();
            case 5 -> filterStaff();
            default -> System.out.println("Invalid option.");
        }
    }

    private void addStaff() {
        System.out.print("Enter Staff Role (Doctor, Pharmacist, Admin): ");
        String role = scanner.nextLine();
        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Staff Gender (Male/Female): ");
        String gender = scanner.nextLine();
        System.out.print("Enter Staff Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Create a new Staff instance and add it to the management system
        Staff newStaff = new Staff(role, name, gender, age);
        staffManagement.addStaff(newStaff);
    }

    private void updateStaff() {
        System.out.print("Enter Staff ID to update (e.g., D001, P002): ");
        String id = scanner.nextLine();
        System.out.print("Enter updated Staff Role: ");
        String role = scanner.nextLine();
        System.out.print("Enter updated Staff Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter updated Staff Gender (Male/Female): ");
        String gender = scanner.nextLine();
        System.out.print("Enter updated Staff Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Create updated Staff object and update it in the management system
        Staff updatedStaff = new Staff(role, name, gender, age);
        updatedStaff.setId(Integer.parseInt(id.substring(1)));  // Keep the same ID number
        staffManagement.updateStaff(id, updatedStaff);
    }

    private void removeStaff() {
        System.out.print("Enter Staff ID to remove (e.g., D001, P002): ");
        String id = scanner.nextLine();
        staffManagement.removeStaff(id);
    }

    private void filterStaff() {
        System.out.print("Enter Role to filter by (or leave blank for all): ");
        String role = scanner.nextLine();
        if (role.isBlank()) role = null;

        System.out.print("Enter Gender to filter by (or leave blank for all): ");
        String gender = scanner.nextLine();
        if (gender.isBlank()) gender = null;

        System.out.print("Enter minimum age: ");
        int minAge = scanner.nextInt();
        System.out.print("Enter maximum age: ");
        int maxAge = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Display filtered staff based on criteria
        staffManagement.displayFilteredStaff(role, gender, minAge, maxAge);
    }

    // Option 2: View Appointments Details
    private void viewAppointmentsDetails() {
        System.out.println("\n--- Appointments Details ---");
        adminSystem.viewAppointmentRecords();
    }

    // Option 3: View and Manage Medication Inventory
    private void manageMedicationInventory() {
        System.out.println("\n--- Medication Inventory Management ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Stock Level");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> adminSystem.viewInventory();
            case 2 -> updateStockLevel();
            default -> System.out.println("Invalid option.");
        }
    }

    private void updateStockLevel() {
        System.out.print("Enter Stock ID: ");
        int stockId = scanner.nextInt();
        System.out.print("Enter new stock level: ");
        int newLevel = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        adminSystem.updateInventory(stockId, newLevel);
    }

    // Option 4: Approve Replenishment Requests
    private void approveReplenishmentRequests() {
        System.out.print("Enter Replenishment Request ID to approve: ");
        int requestId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        adminSystem.approveReplenishmentRequest(requestId);
    }
}
