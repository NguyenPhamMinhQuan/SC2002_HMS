package HMS.Models;

import HMS.Enums.ReplenishStatus;

/**
 * Represents a stock replenish request in the system.
 * This class contains details about a stock request, including the StockID, incoming stock level
 * and the current status of the request.
 */
public class StockReplenishRequest {
    private int ID;
    private int stockId;
    private int incomingStockLevel;
    private ReplenishStatus status;

    /**
     * Constructs a new StockReplenishRequest object with the specified details
     *
     * @param stockId               The identifier of the stock to be replenished.
     * @param incomingStockLevel    The stock level for the incoming stock replenishment.
     * @param status                The status of the Replenish Request.
     */
    public StockReplenishRequest(int stockId, int incomingStockLevel, ReplenishStatus status) {
        this.stockId = stockId;
        this.incomingStockLevel = incomingStockLevel;
        this.status = status;
    }
    /**
     * Gets the unique identifier for this replenish request.
     *
     * @return The ID of the replenish request.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier for this replenish request.
     *
     * @param ID The new ID to be set for the replenish request.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets the identifier of the stock associated with this request.
     *
     * @return The stock ID.
     */
    public int getStockId() {
        return stockId;
    }

    /**
     * Sets the identifier of the stock associated with this request.
     *
     * @param stockId The new stock ID to be set.
     */
    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    /**
     * Gets the level of stock incoming for replenishment.
     *
     * @return The incoming stock level.
     */
    public int getIncomingStockLevel() {
        return incomingStockLevel;
    }

    /**
     * Sets the level of stock incoming for replenishment.
     *
     * @param incomingStockLevel The new incoming stock level to be set.
     */
    public void setIncomingStockLevel(int incomingStockLevel) {
        this.incomingStockLevel = incomingStockLevel;
    }

    /**
     * Gets the current status of the replenish request.
     *
     * @return The replenish status.
     */
    public ReplenishStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the replenish request.
     *
     * @param status The new replenish status to be set.
     */
    public void setStatus(ReplenishStatus status) {
        this.status = status;
    }
}