import java.util.Scanner;

public class PharmacistMenu {
    private PharmacistSystem pharmacistSystem;
    private Scanner scanner;

    // Constructor
    public PharmacistMenu(PharmacistSystem pharmacistSystem) {
        this.pharmacistSystem = pharmacistSystem;
        this.scanner = new Scanner(System.in);
    }

    // Display the pharmacist menu and handle user choices
    public void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Pharmacist Menu ---");
            System.out.println("1. View Appointment Outcome Record");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewAppointmentOutcomeRecord();
                case 2 -> updatePrescriptionStatus();
                case 3 -> viewMedicationInventory();
                case 4 -> submitReplenishmentRequest();
                case 5 -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Option 1: View Appointment Outcome Record
    private void viewAppointmentOutcomeRecord() {
        System.out.print("Enter Appointment ID to view outcome: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();
        pharmacistSystem.viewAppointmentOutcomeRecord(appointmentId);
    }

    // Option 2: Update Prescription Status
    private void updatePrescriptionStatus() {
        System.out.print("Enter Appointment ID to update prescription status: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Prescription Status (e.g., 'Dispensed'): ");
        String status = scanner.nextLine();
        boolean success = pharmacistSystem.updatePrescriptionStatus(appointmentId, status);
        if (!success) {
            System.out.println("Failed to update prescription status. Check Appointment ID.");
        }
    }

    // Option 3: View Medication Inventory
    private void viewMedicationInventory() {
        System.out.println("\n--- Medication Inventory ---");
        pharmacistSystem.viewInventory();
    }

    // Option 4: Submit Replenishment Request
    private void submitReplenishmentRequest() {
        System.out.print("Enter Stock ID to replenish: ");
        int stockId = scanner.nextInt();
        System.out.print("Enter quantity to replenish: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        pharmacistSystem.submitReplenishmentRequest(stockId, quantity);
    }
}
