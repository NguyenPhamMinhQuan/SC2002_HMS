package Repositories;

import Models.AppointmentOutcomeRecord;

import java.util.List;

public interface IAppointmentOutcomeRepository {
    List<AppointmentOutcomeRecord> getAllOutcomes();
    AppointmentOutcomeRecord getOutcomeById(int appointmentID);
    void saveOutcomes(List<AppointmentOutcomeRecord> outcomes);
}
