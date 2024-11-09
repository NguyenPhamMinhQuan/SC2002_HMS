import java.util.Date;

public class AppointmentSchedule {
    private int appointmentID;
    private int patientID;
    private int staffID;
    private Date appointmentDate;
    private String status;

    // Constructor
    public AppointmentSchedule(int appointmentID, int patientID, int staffID, Date appointmentDate, String status) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.staffID = staffID;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for displaying appointment details
    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID + ", Patient ID: " + patientID + ", Staff ID: " + staffID + 
               ", Date: " + appointmentDate + ", Status: " + status;
    }
}
