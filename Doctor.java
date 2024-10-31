import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Doctor {
    private int ID;
    private Appointment[] appointments;
    //private Patient[] patients; //may not need since Appointment already links to patient

    public Doctor(int ID) {
        this.ID = ID;
        appointments = new Appointment[0];
    }

    public int getID() {
        return ID;
    }

    public void viewPatientMedicalRecord(int patientID) {
        //TODO
    }

    public void updatePatientMedicalRecord(int patientID, int medicalRecordID) {
        //TODO
    }

    public void viewPersonalSchedule() {
        //shows the personal schedule: available timings and appointments
        System.out.println("Personal Schedule:");
        for (int i = 0; i < appointments.length; i++) {
            System.out.println(appointments[i]);
        }
    }

    public Appointment setAvailability(LocalDateTime start, LocalDateTime end) {
        Appointment newAppointment = new Appointment(start, end, ID);
        appointments[appointments.length - 1] = newAppointment;
        return appointments[appointments.length - 1];
    }

    public void receiveAppointmentRequest(int appointmentID) {
        char req = 'A';
        for(int i = 0; i < appointments.length; i++){
            if(appointmentID == appointments[i].appointmentID && appointments[i].available){
                System.out.println("Would you like to accept this appointment? (Y/N)");
                Scanner scanner = new Scanner(System.in);
                req = scanner.next().charAt(0);

            }
        }
        if(req == 'Y' || req == 'y'){
            acceptAppointmentRequest(appointmentID);
        }
        else if(req == 'N' || req == 'n'){
            rejectAppointmentRequest(appointmentID);
        }
    }

    private void acceptAppointmentRequest(int appointmentID){
        appointments[appointmentID - 1].updateAppointmentStatus();
    }

    private void rejectAppointmentRequest(int appointmentID){

    }

    public void viewUpcomingAppointments(Appointment[] appointments) {
        for (Appointment appointment : appointments) {
            if(!appointment.available){
                System.out.println(appointment);
            }
        }
    }

    public void recordAppointmentOutcome(int apptID) {
        appointments[apptID - 1].addOutcomeRecord();
    }

}
