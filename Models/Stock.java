package Models;

public class Stock {
    private final String medicineName;
    private final int lowStockAlertThreshold;
    private int ID;
    private int stockLevel;

    public Stock(int ID, String medicineName, int stockLevel, int lowStockAlertThreshold) {
        this.ID = ID;
        this.medicineName = medicineName;
        this.stockLevel = Math.max(stockLevel, 0); // Ensure stock level is non-negative
        this.lowStockAlertThreshold = Math.max(lowStockAlertThreshold, 0); // Ensure threshold is non-negative
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMedicineName() {
        return medicineName;
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

    @Override
    public String toString() {
        return String.format("ID: %d, Medicine: %s, Stock Level: %d, Low Stock Alert Threshold: %d",
                ID, medicineName, stockLevel, lowStockAlertThreshold);
    }
}
