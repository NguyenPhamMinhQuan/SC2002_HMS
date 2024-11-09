import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffManagement {
    private static final String FILENAME = "Staff_List.txt";
    private List<Staff> staffList;

    private int doctorCount = 0;        // Counter for generating unique doctor IDs
    private int pharmacistCount = 0;    // Counter for generating unique pharmacist IDs
    private int adminCount = 0;         // Counter for generating unique admin IDs

    // Constructor that loads staff data from the file
    public StaffManagement() {
        try {
            this.staffList = TextDatabase.loadObjects(FILENAME, Staff.class);
            initializeCounters();
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            this.staffList = new ArrayList<>();
        }
    }

    // Initialize counters based on existing staff in the list
    private void initializeCounters() {
        for (Staff staff : staffList) {
            switch (staff.getRole().toLowerCase()) {
                case "doctor" -> doctorCount++;
                case "pharmacist" -> pharmacistCount++;
                case "admin" -> adminCount++;
            }
        }
    }

    // Save staff data to file
    private void saveStaffToFile() {
        try {
            TextDatabase.saveObjects(FILENAME, staffList);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Generate a unique ID based on role
    private String generateId(String role) {
        int idNumber = 0;
        switch (role.toLowerCase()) {
            case "doctor" -> idNumber = ++doctorCount;
            case "pharmacist" -> idNumber = ++pharmacistCount;
            case "admin" -> idNumber = ++adminCount;
        }
        return String.format("%03d", idNumber); // Format as three digits, e.g., 001
    }

    // Add a new staff member with automatic role-based ID assignment
    public void addStaff(Staff staff) {
        String role = staff.getRole();
        String idNumber = generateId(role);
        staff.setId(Integer.parseInt(idNumber));  // Set ID based on role
        staffList.add(staff);
        saveStaffToFile();
        System.out.println("Staff added: " + staff);
    }

    // Update an existing staff member
    public void updateStaff(String id, Staff updatedStaff) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getId().equals(id)) {
                updatedStaff.setId(Integer.parseInt(id.substring(1))); // Keep same ID
                staffList.set(i, updatedStaff);
                saveStaffToFile();
                System.out.println("Staff updated: " + updatedStaff);
                return;
            }
        }
        System.out.println("Staff not found with ID: " + id);
    }

    // Remove a staff member
    public void removeStaff(String id) {
        staffList.removeIf(s -> s.getId().equals(id));
        saveStaffToFile();
        System.out.println("Staff removed with ID: " + id);
    }

    // Display staff filtered by role, gender, or age range
    public void displayFilteredStaff(String role, String gender, int minAge, int maxAge) {
        for (Staff s : staffList) {
            if ((role == null || s.getRole().equalsIgnoreCase(role)) &&
                (gender == null || s.getGender().equalsIgnoreCase(gender)) &&
                (minAge <= s.getAge() && s.getAge() <= maxAge)) {
                System.out.println(s);
            }
        }
    }
}
