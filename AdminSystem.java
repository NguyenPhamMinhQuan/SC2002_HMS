import java.util.ArrayList;
import java.util.List;

import data.Staff;

public class AdminSystem {
    private List<Staff> staffList = new ArrayList<>();
    private List<AppointmentSchedule> appointmentSchedules = new ArrayList<>();
    private StockSystem stockSystem;  // Reference to StockSystem for inventory management

    // Constructor
    public AdminSystem(StockSystem stockSystem) {
        this.stockSystem = stockSystem;
    }

    // Methods for Staff Management
    public void addStaff(Staff staff) {
        staffList.add(staff);
        System.out.println("Staff added: " + staff.getID());
    }

    public void updateStaff(int staffID, Staff updatedStaff) {
        for (Staff staff : staffList) {
            if (staff.getID() == staffID) {
                staffList.set(staffList.indexOf(staff), updatedStaff);
                System.out.println("Staff updated: " + staffID);
                return;
            }
        }
        System.out.println("Staff not found: " + staffID);
    }

    public void removeStaff(int staffID) {
        staffList.removeIf(staff -> staff.getID() == staffID);
        System.out.println("Staff removed: " + staffID);
    }

    public void viewStaffList() {
        for (Staff staff : staffList) {
            System.out.println(staff);
        }
    }

    // Methods for Appointment Management
    public void viewAppointmentRecords() {
        for (AppointmentSchedule record : appointmentSchedules) {
            System.out.println(record);
        }
    }

    public void updateAppointmentRecords(AppointmentSchedule updatedRecord) {
        for (AppointmentSchedule record : appointmentSchedules) {
            if (record.getAppointmentID() == updatedRecord.getAppointmentID()) {
                appointmentSchedules.set(appointmentSchedules.indexOf(record), updatedRecord);
                System.out.println("Appointment updated: " + updatedRecord.getAppointmentID());
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    // Methods for Inventory Management via StockSystem
    public void viewInventory() {
        List<Stock> inventory = stockSystem.getStocks();
        for (Stock stock : inventory) {
            System.out.println(stock);
        }
    }

    public void updateInventory(int stockID, int newStockLevel) {
        Stock stock = stockSystem.getStockById(stockID);
        if (stock != null) {
            stock.setStockLevel(newStockLevel);
            stockSystem.updateStock(stock);
            System.out.println("Stock updated: " + stock.getMedicineName());
        } else {
            System.out.println("Stock not found.");
        }
    }

    public void approveReplenishmentRequest(int requestID) {
        StockReplenishRequest request = stockSystem.getReplenishRequests().stream()
                .filter(r -> r.getID() == requestID && "Pending".equals(r.getStatus()))
                .findFirst()
                .orElse(null);

        if (request != null) {
            Stock stock = stockSystem.getStockById(request.getStockId());
            if (stock != null) {
                stock.setStockLevel(stock.getStockLevel() + request.getIncomingStockLevel());
                request.setStatus("Approved");
                stockSystem.updateStock(stock);
                stockSystem.updateReplenishRequest(request);
                System.out.println("Replenishment approved for stock ID: " + request.getStockId());
            } else {
                System.out.println("Stock not found for replenishment request.");
            }
        } else {
            System.out.println("Replenishment request not found or already approved.");
        }
    }
}
