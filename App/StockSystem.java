import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockSystem {
    private static final String STOCKS_FILE = "data/stocks.csv";
    private static final String REPLENISH_REQUESTS_FILE = "data/replenish_requests.csv";

    private final List<Stock> stocks;
    private final List<StockReplenishRequest> replenishRequests;

    public StockSystem() {
        this.stocks = new ArrayList<>();
        this.replenishRequests = new ArrayList<>();

        loadStocks();
        loadReplenishRequests();
    }


    /**
     * Stock Methods
     */
    public Stock createStock(Stock newStock) {
        // Check if the stock already exists by ID or name
        for (Stock existingStock : this.stocks) {
            if (existingStock.getID() == newStock.getID() || existingStock.getMedicineName().equalsIgnoreCase(newStock.getMedicineName())) {
                // If it exists, override the existing stock
                existingStock.setMedicineName(newStock.getMedicineName());
                existingStock.setStockLevel(newStock.getStockLevel());
                existingStock.setLowStockAlertThreshold(newStock.getLowStockAlertThreshold());
                saveStocks(); // Save changes to the file
                return existingStock;
            }
        }

        // If it doesn't exist, add it as a new stock
        this.stocks.add(newStock);
        saveStocks(); // Save changes to the file
        return newStock;
    }

    public void deleteStock(Stock stock) {
        this.stocks.remove(stock);
        saveStocks();
    }

    public Stock updateStock(Stock stock) {
        for (Stock currentStock : this.stocks) {
            if (currentStock.getID() == stock.getID()) {
                currentStock.setMedicineName(stock.getMedicineName());
                currentStock.setStockLevel(stock.getStockLevel());
                currentStock.setLowStockAlertThreshold(stock.getLowStockAlertThreshold());
                saveStocks();
                return currentStock;
            }
        }

        return null;
    }

    public void printStocks() {
        System.out.println("Viewing medication inventory...");
        for (Stock stock : this.getStocks()) {
            System.out.println("ID: " + stock.getID());
            System.out.println("Medicine Name: " + stock.getMedicineName());
            System.out.println("Stock Level: " + stock.getStockLevel());
            System.out.println("Low Stock Alert Threshold: " + stock.getLowStockAlertThreshold());
            System.out.println("----------------------------------------");
        }
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
        saveReplenishRequests();
        return stockRequest;
    }

    public boolean deleteReplenishRequest(int stockReplenishRequestId) {
        for (StockReplenishRequest request : replenishRequests) {
            if (request.getID() == stockReplenishRequestId) {
                replenishRequests.remove(request);
                saveReplenishRequests();
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
                saveReplenishRequests();
                return currentRequest;
            }
        }
        return null;
    }

    public void printReplenishRequests() {
        System.out.println("Viewing replenish requests...");
        for (StockReplenishRequest request : this.getReplenishRequests()) {
            System.out.println("Request ID: " + request.getID());
            System.out.println("Stock ID: " + request.getStockId());
            System.out.println("Incoming Stock Level: " + request.getIncomingStockLevel());
            System.out.println("Status: " + request.getStatus());
            System.out.println("----------------------------------------");
        }
    }


    public List<StockReplenishRequest> getReplenishRequests() {
        return this.replenishRequests;
    }

    /**
     * Persistent
     */

    /**
     * Persistence Methods
     */
    public void loadStocks() {
        try (BufferedReader br = new BufferedReader(new FileReader(STOCKS_FILE))) {
            String line;

            // Skip header
            br.readLine();


            while ((line = br.readLine()) != null) {
                String[] stockDetails = line.split(",");
                if (stockDetails.length < 4) {
                    System.err.println("Invalid stock entry: " + line);
                    continue;
                }
                int id = Integer.parseInt(stockDetails[0]);
                String medicineName = stockDetails[1];
                int stockLevel = Integer.parseInt(stockDetails[2]);
                int lowStockAlertThreshold = Integer.parseInt(stockDetails[3]);

                Stock stock = new Stock(id, medicineName, stockLevel, lowStockAlertThreshold);
                this.stocks.add(stock);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Stocks file not found: " + STOCKS_FILE);
        } catch (IOException e) {
            System.err.println("Error reading stocks file: " + e.getMessage());
        }
    }


    public void saveStocks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCKS_FILE))) {
            for (Stock stock : this.stocks) {
                String stockLine = String.join(",", String.valueOf(stock.getID()), stock.getMedicineName(),
                        String.valueOf(stock.getStockLevel()), String.valueOf(stock.getLowStockAlertThreshold()));
                bw.write(stockLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save stocks: " + e.getMessage());
        }
    }


    public void loadReplenishRequests() {
        try (BufferedReader br = new BufferedReader(new FileReader(REPLENISH_REQUESTS_FILE))) {
            String line;

            // Skip header
            br.readLine();


            while ((line = br.readLine()) != null) {
                String[] requestDetails = line.split(",");
                if (requestDetails.length < 4) {
                    System.err.println("Invalid replenish request entry: " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(requestDetails[0]);
                    int stockId = Integer.parseInt(requestDetails[1]);
                    int incomingStockLevel = Integer.parseInt(requestDetails[2]);
                    String status = requestDetails[3];

                    if (incomingStockLevel <= 0) {
                        System.err.println("Invalid replenish request (non-positive stock level): " + line);
                        continue;
                    }

                    StockReplenishRequest request = new StockReplenishRequest(id, stockId, incomingStockLevel, status);
                    this.replenishRequests.add(request);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in replenish request entry: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Replenish requests file not found: " + REPLENISH_REQUESTS_FILE);
        } catch (IOException e) {
            System.err.println("Error reading replenish requests file: " + e.getMessage());
        }
    }


    public void saveReplenishRequests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPLENISH_REQUESTS_FILE))) {
            for (StockReplenishRequest request : this.replenishRequests) {
                String requestLine = String.join(",", String.valueOf(request.getID()), String.valueOf(request.getStockId()),
                        String.valueOf(request.getIncomingStockLevel()), request.getStatus());
                bw.write(requestLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save replenish requests: " + e.getMessage());
        }
    }

}
