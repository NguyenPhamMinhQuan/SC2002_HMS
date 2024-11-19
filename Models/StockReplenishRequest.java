package Models;

import Enums.ReplenishStatus;

public class StockReplenishRequest {
    private int ID;
    private int stockId;
    private int incomingStockLevel;
    private ReplenishStatus status;

    public StockReplenishRequest(int stockId, int incomingStockLevel, ReplenishStatus status) {
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

    public ReplenishStatus getStatus() {
        return status;
    }

    public void setStatus(ReplenishStatus status) {
        this.status = status;
    }
}