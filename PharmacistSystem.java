import java.util.ArrayList;
import java.util.List;

public class PharmacistSystem {
    private List<AppointmentOutcomeRecord> appointmentOutcomes = new ArrayList<>();
    private List<Stock> inventory = new ArrayList<>();
    private List<StockReplenishRequest> replenishRequests = new ArrayList<>();

    // Constructor
    public PharmacistSystem(List<AppointmentOutcomeRecord> appointmentOutcomes, List<Stock> inventory) {
        this.appointmentOutcomes = appointmentOutcomes;
        this.inventory = inventory;
    }

    // Method to view appointment outcome records
    public void viewAppointmentOutcomeRecord(int appointmentId) {
        for (AppointmentOutcomeRecord outcome : appointmentOutcomes) {
            if (outcome.getAppointmentID() == appointmentId) {
                System.out.println(outcome);
                return;
            }
        }
        System.out.println("Appointment outcome record not found for ID: " + appointmentId);
    }

    // Method to update prescription status
    public boolean updatePrescriptionStatus(int appointmentId, String newStatus) {
        for (AppointmentOutcomeRecord outcome : appointmentOutcomes) {
            if (outcome.getAppointmentID() == appointmentId) {
                outcome.setPrescriptionStatus(newStatus); // Assuming setPrescriptionStatus is defined
                System.out.println("Prescription status updated to: " + newStatus);
                return true;
            }
        }
        System.out.println("Failed to update prescription status: Appointment not found.");
        return false;
    }

    // Method to view current inventory
    public void viewInventory() {
        for (Stock stock : inventory) {
            System.out.println(stock);
        }
    }

    // Method to submit replenishment request
    public void submitReplenishmentRequest(int stockId, int amountToReplenish) {
        for (Stock stock : inventory) {
            if (stock.getID() == stockId && stock.getStockLevel() < stock.getLowStockAlertThreshold()) {
                StockReplenishRequest request = new StockReplenishRequest(
                    replenishRequests.size() + 1, stockId, amountToReplenish, "Pending"
                );
                replenishRequests.add(request);
                System.out.println("Replenishment request submitted for stock ID: " + stockId);
                return;
            }
        }
        System.out.println("Stock item either not found or doesn't require replenishment.");
    }

    // Helper method to view pending replenishment requests (for testing or tracking purposes)
    public void viewReplenishmentRequests() {
        for (StockReplenishRequest request : replenishRequests) {
            System.out.println(request);
        }
    }
}
