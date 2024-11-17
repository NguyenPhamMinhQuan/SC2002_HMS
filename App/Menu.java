import java.util.Scanner;

/**
 * Abstract class representing a menu for a specific type of user.
 */
public abstract class Menu {
    protected User user;
    protected Scanner scanner;

    /**
     * Constructs a menu for a specific user.
     * 
     * @param user    the user for whom the menu is created.
     * @param scanner the scanner for input handling.
     */
    public Menu(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Displays the menu options for the user and return the chosen choice
     */
    public abstract int displayOptions();
}
