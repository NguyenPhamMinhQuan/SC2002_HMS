package HMS.Users;

/**
 * Interface representing user-specific functionality in the system.
 */
public interface UserMenuInterface {

    /**
     * Executes a user-specific feature based on the provided option.
     *
     * @param feature the feature option to execute.
     * @return true if the operation was successful, false otherwise.
     */
    boolean functionCall(int feature);
}