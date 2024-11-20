package HMS.Menus;

import HMS.Models.User;
import HMS.Systems.InputHandler;

/**
 * Represents the menu interface for patients.
 */
public class PatientMenu extends Menu {
    /**
     * Constructs a PatientMenu for the given user.
     *
     * @param user the logged-in patient user.
     */
    public PatientMenu(User user) {
        super(user);
    }

    /**
     * Displays menu options for the patient and returns the user's choice.
     *
     * @return the option selected by the user.
     */
    @Override
    public int displayOptions() {
        System.out.println("Patient Menu:");
        System.out.println("0. Change Password.");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
        System.out.print("Choose an option: ");

        return InputHandler.nextInt();


    }
}
