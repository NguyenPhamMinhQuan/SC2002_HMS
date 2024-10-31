import java.time.LocalDateTime;
import java.util.Scanner;

public class Appointment {
    static int numAppointments = 0;
    protected int appointmentID;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected int patientID;
    protected int doctorID;
    protected AppointmentOutcomeRecord apptOut;
    protected boolean available;

    public Appointment(LocalDateTime startTime, LocalDateTime endTime, int doctorID) {
        numAppointments++; //shows the total number of appointments
        appointmentID = numAppointments; //appointmentID is based on the numAppointment
        this.startTime = startTime;
        this.endTime = endTime;
        this.patientID = -1;
        this.doctorID = doctorID;
        apptOut = new AppointmentOutcomeRecord(patientID, doctorID, startTime, "NULL");
        available = true;

    }
    public void updateAppointmentStatus() {
        System.out.println("PatientID: ");
        Scanner sc = new Scanner(System.in);
        int patientID = sc.nextInt();
        this.patientID = patientID;

        System.out.println("Service Type:");
        String serviceType = sc.nextLine();
        apptOut = new AppointmentOutcomeRecord(patientID, doctorID, startTime, serviceType);
        boolean available = false;
    }
    public void addOutcomeRecord() {
        apptOut.addPrescription();
        System.out.println("Enter consultation notes:");
        Scanner sc = new Scanner(System.in);
        String notes = sc.nextLine();
        apptOut.updateConsultationNote(notes);

    }
    public void rescheduleAppointment(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public void notifyOthers(boolean updated) {

    }
}
