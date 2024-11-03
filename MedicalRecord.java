
import java.util.List;

public class MedicalRecord {
	private int patientID;
    private List<Integer> doctorIDs;
    private String name;
    private String dateOfBirth;
    private String gender;
    private int phoneNumber;
    private String email;
    private String bloodType;
    private List<Diagnosis> pastDiagnoses;
    
    // constructor
    public MedicalRecord(int patientID, List<Integer> doctorIDs, String name, String dateOfBirth, String gender, int phoneNumber, String email, String bloodType, List<Diagnosis> pastDiagnoses) {
		this.patientID = patientID;
		this.doctorIDs = doctorIDs;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.bloodType = bloodType;
		this.pastDiagnoses = pastDiagnoses;
}
    
 // Getters
    public int getPatientID() {
        return patientID;
    }

    public List<Integer> getDoctorIDs() {
        return doctorIDs;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<Diagnosis> getPastDiagnoses() {
        return pastDiagnoses;
    }

    // Setters for non-medical personal information
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // to view whole medical record
    public String toString() {
        return "MedicalRecord{" +
                "patientID=" + patientID +
                ", doctorIDs=" + doctorIDs +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", pastDiagnoses=" + pastDiagnoses +
                '}';
    }
	

}
