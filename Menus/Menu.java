package Menus;

import Models.User;

/**
 * Abstract class representing a menu for a specific type of user.
 */
public abstract class Menu implements MenuInterface {
    protected User user;

    /**
     * Constructs a menu for a specific user.
     *
     * @param user the user for whom the menu is created.
     */
    public Menu(User user) {
        this.user = user;
    }

    /**
     * Displays the menu options for the user and return the chosen choice
     */
    public abstract int displayOptions();
}
