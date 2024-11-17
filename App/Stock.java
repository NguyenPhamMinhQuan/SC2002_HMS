public class Stock {
    private int ID;
    private String medicineName;
    private int stockLevel;
    private int lowStockAlertThreshold;

    public Stock() {}

    public Stock(int ID, String medicineName, int stockLevel, int lowStockAlertThreshold) {
        this.ID = ID;
        this.medicineName = medicineName;
        this.stockLevel = stockLevel;
        this.lowStockAlertThreshold = lowStockAlertThreshold;
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

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    public void setLowStockAlertThreshold(int lowStockAlertThreshold) {
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }
}
