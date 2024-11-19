package Systems;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Utility class for handling user input and validation.
 * Provides methods for retrieving and validating various types of input from the user.
 */
public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    // Private constructor to prevent instantiation
    private InputHandler() {
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the input string entered by the user.
     */
    public static String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Reads an integer input from the user.
     *
     * @return the integer entered by the user.
     */
    public static int nextInt() {
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return value;
    }

    /**
     * Reads a double input from the user.
     *
     * @return the double value entered by the user.
     */
    public static double nextDouble() {
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        return value;
    }

    /**
     * Closes the scanner used for input.
     * Should be called when input is no longer required to release resources.
     */
    public static void close() {
        scanner.close();
    }

    /**
     * Prompts the user for input and validates it using a provided predicate.
     * Does NOT allow exiting by typing "exit". The user must provide valid input.
     *
     * @param prompt       the message to display to the user.
     * @param errorMessage the error message to display if the input is invalid.
     * @param validator    a predicate to validate the input.
     * @return the validated input if valid.
     */
    public static String getValidatedInput(String prompt, String errorMessage, Predicate<String> validator) {
        while (true) {
            System.out.println(prompt);

            try {
                String input = nextLine();

                // Do NOT allow exiting
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting is not allowed for this input. Please provide valid input.");
                    continue;
                }

                // Validate the input
                if (validator.test(input)) {
                    return input;
                }

                System.out.println(errorMessage);
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Prompts the user for input and validates it using a provided predicate.
     * Allows exiting by typing "exit" (case-insensitive), which returns {@code null}.
     *
     * @param prompt       the message to display to the user.
     * @param errorMessage the error message to display if the input is invalid.
     * @param validator    a predicate to validate the input.
     * @return the validated input if valid; {@code null} if the user enters "exit".
     */
    public static String getValidatedInputWithExit(String prompt, String errorMessage, Predicate<String> validator) {
        while (true) {
            System.out.println(prompt);

            try {
                String input = nextLine();

                // Exit condition: If the input is "exit" (case-insensitive), return null
                if (input.equalsIgnoreCase("exit")) {
                    return null; // Returning null signals the user wants to exit
                }

                // Validate the input
                if (validator.test(input)) {
                    return input;
                }

                System.out.println(errorMessage);
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Functional interface to represent a provider for input.
     *
     * @param <T> the type of input provided.
     */
    @FunctionalInterface
    public interface InputProvider<T> {
        /**
         * Provides an input of type {@code T}.
         *
         * @return the input value.
         * @throws Exception if an error occurs while retrieving input.
         */
        T getInput() throws Exception;
    }
}