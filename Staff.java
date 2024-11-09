public class Staff {
    private String id;        // ID with role prefix (e.g., D001)
    private String name;      // Staff name
    private String role;      // Role of the staff (Doctor, Pharmacist, Admin)
    private String gender;    // Gender of the staff
    private int age;          // Age of the staff

    // Constructor
    public Staff(String role, String name, String gender, int age) {
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    // Generate role-based ID
    public void setId(int idNumber) {
        String prefix = "";
        switch (role.toLowerCase()) {
            case "doctor" -> prefix = "D";
            case "pharmacist" -> prefix = "P";
            case "admin" -> prefix = "A";
        }
        this.id = prefix + String.format("%03d", idNumber); // e.g., D001, P001
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Staff[ID=" + id + ", Name=" + name + ", Role=" + role +
                ", Gender=" + gender + ", Age=" + age + "]";
    }
}
