package HMS.Models;

/**
 * Represents a stock entry for a specific medicine in the hospital management system.
 * This class maintains information about the medicine, stock levels, and alert thresholds.
 */
public class Stock {
    private final String medicineName;
    private final int lowStockAlertThreshold;
    private int ID;
    private int stockLevel;

    /**
     * Constructs a new {@code Stock} instance.
     *
     * @param ID                     the unique identifier for the stock entry.
     * @param medicineName           the name of the medicine.
     * @param stockLevel             the initial stock level, must be non-negative.
     * @param lowStockAlertThreshold the threshold below which a stock alert is triggered, must be non-negative.
     */
    public Stock(int ID, String medicineName, int stockLevel, int lowStockAlertThreshold) {
        this.ID = ID;
        this.medicineName = medicineName;
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
        this.lowStockAlertThreshold = Math.max(lowStockAlertThreshold, 0); // Ensure threshold is non-negative
    }

    /**
     * Gets the unique identifier for the stock entry.
     *
     * @return the stock entry ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier for the stock entry.
     *
     * @param ID the stock entry ID to set.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets the name of the medicine.
     *
     * @return the medicine name.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Gets the current stock level of the medicine.
     *
     * @return the stock level.
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Sets the current stock level of the medicine. Ensures the stock level is non-negative.
     *
     * @param stockLevel the new stock level to set.
     */
    public void setStockLevel(int stockLevel) {
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
    }

    /**
     * Gets the low stock alert threshold for the medicine.
     *
     * @return the low stock alert threshold.
     */
    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    /**
     * Returns a string representation of the stock entry.
     *
     * @return a formatted string containing the stock ID, medicine name, stock level, and alert threshold.
     */
    @Override
    public String toString() {
        return String.format("ID: %d, Medicine: %s, Stock Level: %d, Low Stock Alert Threshold: %d",
                ID, medicineName, stockLevel, lowStockAlertThreshold);
    }
}
