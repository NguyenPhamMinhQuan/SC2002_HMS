import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockSystem {

    private final List<Stock> stocks;
    private final List<StockReplenishRequest> replenishRequests;

    public StockSystem() {
        this.stocks = new ArrayList<>();
        this.replenishRequests = new ArrayList<>();
    }

    /**
     * Stock Methods
     */
    public Stock createStock(Stock newStock) {
        this.stocks.add(newStock);
        return newStock;
    }

    public void deleteStock(Stock stock) {
        this.stocks.remove(stock);
    }

    public Stock updateStock(Stock stock) {
        for (Stock currentStock : this.stocks) {
            if (currentStock.getID() == stock.getID()) {
                currentStock.setMedicineName(stock.getMedicineName());
                currentStock.setStockLevel(stock.getStockLevel());
                currentStock.setLowStockAlertThreshold(stock.getLowStockAlertThreshold());

                return currentStock;
            }
        }

        return null;
    }

    public List<Stock> getStocks() {
        return this.stocks;
    }

    public List<Stock> getLowLevelStocks() {
        return this.stocks.stream()
                .filter(stock -> stock.getStockLevel() <= stock.getLowStockAlertThreshold())
                .collect(Collectors.toList());
    }

    public Stock getStockById(int stockId) {
        return this.stocks.stream()
                .filter(stock -> stock.getID() == stockId)
                .findFirst()
                .orElse(null);
    }

    /**
     * StockReplenishRequest Methods
     */
    public StockReplenishRequest createReplenishRequest(StockReplenishRequest stockRequest) {
        this.replenishRequests.add(stockRequest);
        return stockRequest;
    }

    public boolean deleteReplenishRequest(int stockReplenishRequestId) {
        for (StockReplenishRequest request : replenishRequests) {
            if (request.getID() == stockReplenishRequestId) {
                replenishRequests.remove(request);
                return true;
            }
        }
        return false;
    }

    public StockReplenishRequest updateReplenishRequest(StockReplenishRequest request) {
        for (StockReplenishRequest currentRequest : replenishRequests) {
            if (currentRequest.getID() == request.getID()) {
                currentRequest.setStockId(request.getStockId());
                currentRequest.setIncomingStockLevel(request.getIncomingStockLevel());
                currentRequest.setStatus(request.getStatus());
                return currentRequest;
            }
        }
        return null;
    }

    public List<StockReplenishRequest> getReplenishRequests() {
        return this.replenishRequests;
    }
}
