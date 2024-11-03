import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
	private MedicalRecord medicalRecord;
    private List<AppointmentRecord> appointmentRecords;
    
    public Patient(int ID, String userType, MedicalRecord medicalRecord) {
        super(ID, userType);
        this.medicalRecord = medicalRecord;
        this.appointmentRecords = new ArrayList<>();
    }
    
    public void viewMedicalRecord() {
        System.out.println("Medical Record for Patient ID: " + getID());
        System.out.println("Name: " + medicalRecord.getName());
        System.out.println("Date of Birth: " + medicalRecord.getDateOfBirth());
        System.out.println("Gender: " + medicalRecord.getGender());
        System.out.println("Phone Number: " + medicalRecord.getPhoneNumber());
        System.out.println("Email: " + medicalRecord.getEmail());
        System.out.println("Blood Type: " + medicalRecord.getBloodType());
        System.out.println("Past Diagnoses: " + medicalRecord.getPastDiagnoses());
    }
    
    public void updatePersonalInfo(String newEmail, int newPhoneNumber) {
        medicalRecord.setEmail(newEmail);
        medicalRecord.setPhoneNumber(newPhoneNumber);
        System.out.println("Personal information updated successfully.");
    }
    
    public void viewAvailableAppointments(List<AppointmentRecord> availableAppointments) {
        System.out.println("Available Appointment Slots:");
        for (AppointmentRecord appointment : availableAppointments) {
            System.out.println("Doctor ID: " + appointment.getDoctorID() +
                    " | Date & Time: " + appointment.getAppointmentDateTime());
        }
    }
    
    public void scheduleAppointment(AppointmentRecord appointment) {
        appointmentRecords.add(appointment);
        System.out.println("Appointment scheduled successfully for " + appointment.getAppointmentDateTime());
    }
    
    public void rescheduleAppointment(AppointmentRecord oldAppointment, AppointmentRecord newAppointment) {
        if (appointmentRecords.contains(oldAppointment)) {
            appointmentRecords.remove(oldAppointment);
            appointmentRecords.add(newAppointment);
            System.out.println("Appointment rescheduled to " + newAppointment.getAppointmentDateTime());
        } else {
            System.out.println("The specified appointment was not found.");
        }
    }
    
    public void cancelAppointment(AppointmentRecord appointment) {
        if (appointmentRecords.contains(appointment)) {
            appointmentRecords.remove(appointment);
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("The specified appointment was not found.");
        }
    }
    
    // add public void viewScheduledAppointments() ??
    // add public void viewPastAppointmentOutcomeRecords() ??
    
    
}

