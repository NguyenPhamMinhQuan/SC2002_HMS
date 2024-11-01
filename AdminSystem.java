import java.util.ArrayList;
import java.util.List;

import data.Staff;

public class AdminSystem {
    private List<Staff> staffList = new ArrayList<>();
    private List<Patient> patientList = new ArrayList<>();
    private List<AppointmentSchedule> appointmentSchedules = new ArrayList<>();
    private List<Stock> inventory = new ArrayList<>();
    private List<StockReplenishRequest> replenishRequests = new ArrayList<>();

    // Constructor
    public AdminSystem() {}

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

    // Methods for Inventory Management
    public void viewInventory() {
        for (Stock stock : inventory) {
            System.out.println(stock);
        }
    }

    public void updateInventory(int stockID, int newStockLevel) {
        for (Stock stock : inventory) {
            if (stock.getID() == stockID) {
                stock.setStockLevel(newStockLevel);
                System.out.println("Stock updated: " + stock.getMedicineName());
                return;
            }
        }
        System.out.println("Stock not found.");
    }

    public void approveReplenishmentRequest(int requestID) {
        for (StockReplenishRequest request : replenishRequests) {
            if (request.getID() == requestID && "Pending".equals(request.getStatus())) {
                Stock stock = getStockById(request.getStockId());
                if (stock != null) {
                    stock.setStockLevel(stock.getStockLevel() + request.getIncomingStockLevel());
                    request.setStatus("Approved");
                    System.out.println("Replenishment approved for stock ID: " + request.getStockId());
                    return;
                }
            }
        }
        System.out.println("Replenishment request not found or already approved.");
    }

    // Helper method to get Stock by ID
    private Stock getStockById(int stockID) {
        for (Stock stock : inventory) {
            if (stock.getID() == stockID) {
                return stock;
            }
        }
        return null;
    }
}
