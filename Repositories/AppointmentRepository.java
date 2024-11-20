package HMS.Repositories;

import HMS.Models.Appointment;
import HMS.Enums.AppointmentStatus;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentRepository implements LoadandSaveInterface<Appointment> {

    private static final String APPOINTMENTS_FILE = "data/appointments.csv";

    /**
     * Loads the appointment data from the file and converts date strings into Date objects.
     *
     * @return a list of appointments loaded from the file.
     */
    @Override
    public List<Appointment> loadData() {
        List<Appointment> appointments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            reader.readLine(); // Skip the header line

            // Create a SimpleDateFormat to parse the date string into a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 5) { // Expecting 5 values: ID, PatientID, DoctorID, Status, Date
                    int appointmentID = Integer.parseInt(data[0]);
                    String patientID = data[1];
                    String doctorID = data[2];
                    AppointmentStatus statusEnum = AppointmentStatus.valueOf(data[3].toUpperCase());

                    // Parse the date string into a Date object
                    Date appointmentDate = sdf.parse(data[4]);

                    // Create a new Appointment object and add it to the list
                    Appointment appointment = new Appointment(appointmentID, patientID, doctorID, statusEnum, appointmentDate);
                    appointments.add(appointment);
                }
            }
        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Saves a list of appointments to the file.
     *
     * @param data the list of appointments to save.
     */
    @Override
    public void saveData(List<Appointment> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            writer.write("AppointmentID,PatientID,DoctorID,Status,Date");
            writer.newLine();

            // Create a SimpleDateFormat to format the Date object back to a string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Appointment appointment : data) {
                // Write each appointment's data to the file
                writer.write(String.join(",",
                        String.valueOf(appointment.getID()),
                        appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getAppointmentStatus().toString(),
                        sdf.format(appointment.getAppointmentDate()) // Convert Date to string
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
