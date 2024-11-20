package Menus;

import Models.User;
import Systems.InputHandler;

/**
 * Represents the menu interface for doctors.
 */
public class DoctorMenu extends Menu {
    /**
     * Constructs a new DoctorMenu for the given user.
     *
     * @param user the user object representing the doctor who is logging in
     */
    public DoctorMenu(User user) {
        super(user);
    }

    /**
     * Displays the available options in the Doctor's menu.
     * The user can select an option by entering the corresponding number.
     *
     * @return the number corresponding to the selected option
     */
    @Override
    public int displayOptions() {
        System.out.println("Doctor Menu:");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Manage Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
        System.out.print("Choose an option: ");

        return InputHandler.nextInt();
    }
}
