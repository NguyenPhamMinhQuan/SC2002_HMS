import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockSystem {

    private final String stockFilename = "stock.txt";
    private final String stockReplenishRequestFilename = "stockreplenishrequest.txt";

    private final List<Stock> stocks;
    private final List<StockReplenishRequest> replenishRequests;

    public StockSystem() {
        this.stocks = loadStocksFromFile();
        this.replenishRequests = loadReplenishRequestsFromFile();
    }

    /**
     * File Accessing Methods
     */
    private List<Stock> loadStocksFromFile() {
        try {
            return TextDatabase.loadObjects(stockFilename, Stock.class);
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            System.out.println("Error loading stocks from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<StockReplenishRequest> loadReplenishRequestsFromFile() {
        try {
            return TextDatabase.loadObjects(stockReplenishRequestFilename, StockReplenishRequest.class);
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            System.out.println("Error loading replenish requests from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Save stocks to file
    private void saveStocksToFile() {
        try {
            TextDatabase.saveObjects(stockFilename, this.stocks);
        } catch (IOException | IllegalAccessException e) {
            System.out.println("Error saving stocks to file: " + e.getMessage());
        }
    }

    // Save replenish requests to file
    private void saveReplenishRequestsToFile() {
        try {
            TextDatabase.saveObjects(stockReplenishRequestFilename, this.replenishRequests);
        } catch (IOException | IllegalAccessException e) {
            System.out.println("Error saving replenish requests to file: " + e.getMessage());
        }
    }

    /**
     * Stock Methods
     */
    public Stock createStock(Stock newStock) {
        this.stocks.add(newStock);
        saveStocksToFile();
        return newStock;
    }

    public void deleteStock(Stock stock) {
        this.stocks.remove(stock);
        saveStocksToFile();
    }

    public Stock updateStock(Stock stock) {
        for (Stock currentStock : this.stocks) {
            if (currentStock.getID() == stock.getID()) {
                currentStock.setMedicineName(stock.getMedicineName());
                currentStock.setStockLevel(stock.getStockLevel());
                currentStock.setLowStockAlertThreshold(stock.getLowStockAlertThreshold());
                saveStocksToFile();

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
        saveReplenishRequestsToFile();
        return stockRequest;
    }

    public boolean deleteReplenishRequest(int stockReplenishRequestId) {
        for (StockReplenishRequest request : replenishRequests) {
            if (request.getID() == stockReplenishRequestId) {
                replenishRequests.remove(request);
                saveReplenishRequestsToFile();
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
                saveReplenishRequestsToFile();
                return currentRequest;
            }
        }
        return null;
    }

    public List<StockReplenishRequest> getReplenishRequests() {
        return this.replenishRequests;
    }
}
