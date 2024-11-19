package Enums;

public enum Dispensed {
    YES(true),
    NO(false);

    private final boolean value;

    // Constructor to assign the boolean value
    Dispensed(boolean value) {
        this.value = value;
    }

    // Getter to access the boolean value
    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value ? "true" : "false";
    }
}
