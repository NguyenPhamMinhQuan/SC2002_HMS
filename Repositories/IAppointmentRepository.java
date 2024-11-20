package Repositories;

import Enums.AppointmentStatus;
import Models.Appointment;

import java.util.List;

/**
 * Interface for managing appointment data storage and retrieval.
 */
public interface IAppointmentRepository {
    /**
     * Retrieves all appointments.
     *
     * @return a list of all appointments.
     */
    List<Appointment> getAllAppointments();

    /**
     * Retrieves appointments for a specific doctor based on their ID and status.
     *
     * @param doctorId the ID of the doctor.
     * @param status   the status of the appointments to filter.
     * @return a list of appointments matching the criteria.
     */
    List<Appointment> getAppointmentsByDoctor(String doctorId, AppointmentStatus status);

    /**
     * Retrieves appointments for a specific patient based on their ID.
     *
     * @param patientId the ID of the patient.
     * @return a list of appointments for the patient.
     */
    List<Appointment> getAppointmentsByPatient(String patientId);

    /**
     * Finds a specific appointment by its ID.
     *
     * @param appointmentId the ID of the appointment.
     * @return the appointment with the given ID, or null if not found.
     */
    Appointment findById(int appointmentId);

    /**
     * Saves all appointments to the storage.
     *
     * @param appointments the list of appointments to save.
     */
    void saveAllAppointments(List<Appointment> appointments);
}

