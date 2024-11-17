import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    private InputHandler() {
        // Private constructor to prevent instantiation
    }

    public static String nextLine() {
        return scanner.nextLine();
    }

    public static int nextInt() {
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return value;
    }

    public static double nextDouble() {
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        return value;
    }

    // Add other input handling methods as needed
    public static void close() {
        scanner.close();
    }
}
