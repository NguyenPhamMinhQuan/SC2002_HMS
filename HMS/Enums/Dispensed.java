package HMS.Enums;

/**
 * Enum representing whether a medication has been dispensed or not.
 * This enum holds a boolean value to indicate if the medication is dispensed (YES) or not (NO).
 */
public enum Dispensed {
    YES(true),
    NO(false);

    private final boolean value;

    /**
     * Constructor for the enum, assigning a boolean value to each status.
     *
     * @param value the boolean value indicating whether the medication has been dispensed (true) or not (false)
     */
    // Constructor to assign the boolean value
    Dispensed(boolean value) {
        this.value = value;
    }

    /**
     * Returns the boolean value of the dispensed status.
     *
     * @return the boolean value: true if dispensed, false if not
     */
    // Getter to access the boolean value
    public boolean getValue() {
        return value;
    }

    /**
     * Returns the string representation of the dispensed status.
     * The string is returned as "true" if the value is true, and "false" if the value is false.
     *
     * @return a string representing the boolean value ("true" or "false")
     */
    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }
}
