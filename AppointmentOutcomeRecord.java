import java.time.LocalDateTime;
import java.util.Scanner;

public class AppointmentOutcomeRecord {
    //protected boolean furtherActionRequired; //inform other doctors whether further actions are necessary
    protected int patientID;
    protected int doctorID;
    protected LocalDateTime appointmentDateTime;
    protected String serviceType;
    protected int[] prescription; //Medication Class?
    //protected String prescriptionStatus; //can be under medication Class?
    protected String consultationNotes;

    public boolean verifyDoctorAccess(int doctorID) {
        return this.doctorID == doctorID;
    }

    public boolean verifyPatientAccess(int patientID) {
        return this.patientID == patientID;
    }

    //public boolean verifyPharmacistAccess(int Pharmacist) {}

    public AppointmentOutcomeRecord(int patientID, int doctorID, LocalDateTime appointmentDateTime, String serviceType) {
        //created in the appointment record
        this.appointmentDateTime = appointmentDateTime;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.serviceType = serviceType;
        prescription = new int[0];
        consultationNotes = null;
    }
    private void updateDateTime(LocalDateTime appointmentDate) {
        // automatic update when appointmentRecord is updated
        appointmentDateTime = appointmentDate;
    }
    private void updateServiceType(String serviceType){
        this.serviceType = serviceType;
    }

    public void addPrescription() {
        System.out.print("Number of medication prescribed: ");
        Scanner sc = new Scanner(System.in);
        int numMed = sc.nextInt();
        for(int i = 0; i < numMed; i++){
            System.out.print("Medication prescribed: ");
            int stockID = sc.nextInt();
            prescription[i] = stockID;
        }

    }
    private void promptDoctorToUpdateMedicalRecord(boolean furtherActionRequired) {

    }

    public void updateConsultationNote(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }
    public boolean verifyReadAccess(int PatientID, int pharmacist) {
        return true;
    }
    private void view() {
        System.out.println("PatientID: " + patientID);
        System.out.println("DoctorID: " + doctorID);
        System.out.println("AppointmentDateTime: " + appointmentDateTime);
        System.out.println("ServiceType: " + serviceType);
        System.out.println("Prescription: ");
        for(int i = 0; i < prescription.length; i++){
            System.out.println("\t" + prescription[i]);
        }
        System.out.println("ConsultationNotes: " + consultationNotes);
    }
}
