package data;

public class Staff {
    private int id;
    private String name;
    private String position;
    private String department;

    // Constructor
    public Staff(int id, String name, String position, String department) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.department = department;
    }

    // Getters and Setters
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // toString method for displaying staff details
    @Override
    public String toString() {
        return "Staff ID: " + id + ", Name: " + name + ", Position: " + position + ", Department: " + department;
    }
    
}
