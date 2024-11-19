package Enums;

public enum ReplenishStatus {
    PENDING("pending"),
    APPROVED("approved"),
    COMPLETED("completed"),
    REJECTED("rejected");

    private final String value;

    // Constructor to assign the lowercase value
    ReplenishStatus(String value) {
        this.value = value;
    }

    // Override toString to return the lowercase value
    @Override
    public String toString() {
        return value;
    }
}
