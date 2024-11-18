package Models;

public class Stock {
    private int ID;
    private String medicineName;
    private int stockLevel;
    private int lowStockAlertThreshold;

    /**
     * Default constructor.
     */
    public Stock() {
    }

    /**
     * Full constructor for initializing all fields.
     *
     * @param ID                     the unique ID of the stock item.
     * @param medicineName           the name of the medicine or item.
     * @param stockLevel             the current stock level.
     * @param lowStockAlertThreshold the threshold to trigger low stock alerts.
     */
    public Stock(int ID, String medicineName, int stockLevel, int lowStockAlertThreshold) {
        this.ID = ID;
        this.medicineName = medicineName;
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
        this.lowStockAlertThreshold = Math.max(lowStockAlertThreshold, 0); // Ensure threshold is non-negative
    }

    /**
     * Simplified constructor for prescribed medications (low stock alert not relevant).
     *
     * @param medicineName the name of the medicine.
     * @param stockLevel   the quantity prescribed or used.
     */
    public Stock(String medicineName, int stockLevel) {
        this.medicineName = medicineName;
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
        this.lowStockAlertThreshold = 0; // Default threshold
    }

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
    }

    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    public void setLowStockAlertThreshold(int lowStockAlertThreshold) {
        this.lowStockAlertThreshold = Math.max(lowStockAlertThreshold, 0); // Ensure threshold is non-negative
    }

    /**
     * Checks if the current stock level is below the low stock alert threshold.
     *
     * @return true if stock is low; false otherwise.
     */
    public boolean isLowStock() {
        return stockLevel < lowStockAlertThreshold;
    }

    /**
     * Returns a string representation of the stock item.
     *
     * @return a string describing the stock item.
     */
    @Override
    public String toString() {
        return String.format("ID: %d, Medicine: %s, Stock Level: %d, Low Stock Alert Threshold: %d",
                ID, medicineName, stockLevel, lowStockAlertThreshold);
    }
}
