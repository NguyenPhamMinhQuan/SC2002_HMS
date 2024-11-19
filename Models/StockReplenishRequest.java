package Models;

public class StockReplenishRequest {
    private int ID;
    private int stockId;
    private int incomingStockLevel;
    private String status;

    public StockReplenishRequest(int stockId, int incomingStockLevel, String status) {
        this.stockId = stockId;
        this.incomingStockLevel = incomingStockLevel;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getIncomingStockLevel() {
        return incomingStockLevel;
    }

    public void setIncomingStockLevel(int incomingStockLevel) {
        this.incomingStockLevel = incomingStockLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}