package Systems;

import Models.Stock;
import Models.StockReplenishRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockSystem {
    private static final String STOCKS_FILE = "data/stocks.csv";
    private static final String REPLENISH_REQUESTS_FILE = "data/replenish_requests.csv";

    private static final List<Stock> stocks = new ArrayList<>();
    private static final List<StockReplenishRequest> replenishRequests = new ArrayList<>();
    private static int nextReplenishRequestID = 1; // Static ID tracker for replenish requests

    static {
        loadStocks();
        loadReplenishRequests();
    }

    // Static method to get all stocks
    public static List<Stock> getStocks() {
        return stocks;
    }

    // Static method to get stocks with low levels
    public static List<Stock> getLowLevelStocks() {
        return stocks.stream()
                .filter(stock -> stock.getStockLevel() <= stock.getLowStockAlertThreshold())
                .collect(Collectors.toList());
    }

    // Static method to create a replenish request
    public static StockReplenishRequest createReplenishRequest(StockReplenishRequest stockRequest) {
        stockRequest.setID(nextReplenishRequestID++);

        for (StockReplenishRequest existingRequest : replenishRequests) {
            if (existingRequest.getStockId() == stockRequest.getStockId() &&
                    existingRequest.getStatus().equalsIgnoreCase("pending")) {
                existingRequest.setIncomingStockLevel(stockRequest.getIncomingStockLevel());
                saveReplenishRequests();
                return existingRequest;
            }
        }

        replenishRequests.add(stockRequest);
        saveReplenishRequests();
        return stockRequest;
    }

    // Static method to update a replenish request
    public static StockReplenishRequest updateReplenishRequest(StockReplenishRequest request) {
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

    // Static method to get all replenish requests with "pending" status
    public static List<StockReplenishRequest> getReplenishRequests() {
        return replenishRequests.stream()
                .filter(x -> x.getStatus().equalsIgnoreCase("pending"))
                .collect(Collectors.toList());
    }
    public static void showLowStockItemsAndCreateReplenishRequest() {
        List<Stock> lowLevelStocks = StockSystem.getLowLevelStocks();  // Get stocks that are low level

        if (lowLevelStocks.isEmpty()) {
            System.out.println("No low stock items found.");
            return;
        }

        // Display the low-level stocks
        System.out.println("\n--- Low Stock Items ---");
        System.out.println("+-----+--------------------------+---------------+--------------------+");
        System.out.println("| No. | Medicine Name            | Stock Level   | Low Stock Threshold |");
        System.out.println("+-----+--------------------------+---------------+--------------------+");

        for (int i = 0; i < lowLevelStocks.size(); i++) {
            Stock stock = lowLevelStocks.get(i);
            System.out.printf("| %-3d | %-24s | %-13d | %-18d |\n",
                    i + 1,
                    stock.getMedicineName(),
                    stock.getStockLevel(),
                    stock.getLowStockAlertThreshold());
        }

        System.out.println("+-----+--------------------------+---------------+--------------------+");

        // Ask the user to select a stock to replenish
        String stockId = InputHandler.getValidatedInput(
                "Enter the number of the stock to create a replenish request (or type 'exit' to cancel): ",
                "Invalid input. Please enter a valid number or 'exit'.",
                input -> input.equalsIgnoreCase("exit") || isValidStockSelection(input, lowLevelStocks.size())
        );

        if (stockId.equalsIgnoreCase("exit")) {
            System.out.println("Operation canceled.");
            return;
        }

        int selectedStockIndex = Integer.parseInt(stockId) - 1;
        Stock selectedStock = lowLevelStocks.get(selectedStockIndex);

        // Ask how much stock to replenish (based on the low stock threshold and current stock level)
        int maxQuantity = selectedStock.getLowStockAlertThreshold() * 2;
        String replenishQuantityInput = InputHandler.getValidatedInput(
                "Enter the quantity to replenish (max " + maxQuantity + "): ",
                "Invalid quantity. Must be a number between 1 and " + maxQuantity + ".",
                input -> {
                    try {
                        int quantity = Integer.parseInt(input);
                        return quantity > 0 && quantity <= maxQuantity;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
        );

        int replenishQuantity = Integer.parseInt(replenishQuantityInput);

        // Create replenish request
        StockReplenishRequest replenishRequest = new StockReplenishRequest(
                selectedStock.getID(),
                replenishQuantity,  // Quantity to replenish
                "pending"           // Initial status is "pending"
        );

        // Add the replenish request to the system
        StockSystem.createReplenishRequest(replenishRequest);
        System.out.println("Replenishment request created successfully for " + selectedStock.getMedicineName() + " with quantity " + replenishQuantity + ".");
    }

    // Helper function to validate stock selection
    private static boolean isValidStockSelection(String input, int size) {
        try {
            int index = Integer.parseInt(input);
            return index > 0 && index <= size;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Static method to print all stocks in a table format
    public static void printStocks() {
        // Check if there are any stocks
        if (getStocks().isEmpty()) {
            System.out.println("No stocks available.");
            return;
        }

        // Printing the header row
        System.out.println("+-----+--------------------------+---------------+------------------------+");
        System.out.println("| No. | Medicine Name            | Stock Level   | Low Stock Threshold     |");
        System.out.println("+-----+--------------------------+---------------+------------------------+");

        // Printing each stock row
        for (int i = 0; i < getStocks().size(); i++) {
            Stock stock = getStocks().get(i);
            System.out.printf("| %-3d | %-24s | %-13d | %-22d |\n",
                    i + 1,
                    stock.getMedicineName(),
                    stock.getStockLevel(),
                    stock.getLowStockAlertThreshold());
        }

        // Printing the footer row
        System.out.println("+-----+--------------------------+---------------+------------------------+");
    }

    public static void displayPendingReplenishRequest() {
        if (getReplenishRequests().isEmpty()) {
            System.out.println("No replenish requests available.");
            return;
        }

        // Displaying the table header
        System.out.println("+-------------------+-------------------+-------------------+-------------------+");
        System.out.println("| Request ID        | Stock ID          | Incoming Stock    | Status            |");
        System.out.println("+-------------------+-------------------+-------------------+-------------------+");

        // Displaying each replenish request in the table
        for (StockReplenishRequest request : getReplenishRequests()) {
            System.out.printf("| %-17d | %-17d | %-17d | %-17s |\n",
                    request.getID(),
                    request.getStockId(),
                    request.getIncomingStockLevel(),
                    request.getStatus());
        }

        System.out.println("+-------------------+-------------------+-------------------+-------------------+");
    }

    public static void handleReplenishRequests() {
        List<StockReplenishRequest> pendingRequests = getReplenishRequests();  // Assuming this method returns only pending requests

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending replenish requests available.");
            return;  // Exit if no requests are pending
        }

        displayPendingReplenishRequest();

        String selectedRequestID = InputHandler.getValidatedInput(
                "Enter the Request ID to approve/reject or type 'exit' to cancel: ",
                "Invalid input. Please enter a valid Request ID or 'exit'.",
                input -> input.equalsIgnoreCase("exit") || isValidReplenishRequestSelection(input)
        );

        if (selectedRequestID.equalsIgnoreCase("exit")) {
            System.out.println("Exiting replenish request handling.");
            return;
        }

        int requestID = Integer.parseInt(selectedRequestID);
        StockReplenishRequest request = getReplenishRequestByID(requestID);

        if (request == null) {
            System.out.println("Replenish request not found.");
            return;
        }

        // Step 3: Ask admin to approve or reject the request
        String decision = InputHandler.getValidatedInput(
                "Approve or Reject this request? (A/R): ",
                "Invalid input. Please enter 'A' to approve or 'R' to reject.",
                input -> input.equalsIgnoreCase("A") || input.equalsIgnoreCase("R")
        );

        if (decision.equalsIgnoreCase("A")) {
            approveReplenishRequest(request);
            System.out.println("Request approved and stock updated.");
        } else if (decision.equalsIgnoreCase("R")) {
            rejectReplenishRequest(request);
            System.out.println("Request rejected.");
        }
    }

    private static void approveReplenishRequest(StockReplenishRequest request) {
        Stock stock = getStockById(request.getStockId());
        if (stock != null) {
            // Increase the stock level by the incoming amount
            stock.setStockLevel(stock.getStockLevel() + request.getIncomingStockLevel());
            saveStocks(); // Save updated stock to the file
        } else {
            System.out.println("Stock not found for Stock ID: " + request.getStockId());
        }

        // Update the replenish request status to "approved"
        request.setStatus("approved");
        saveReplenishRequests(); // Save updated replenish requests to the file
    }

    private static void rejectReplenishRequest(StockReplenishRequest request) {
        // Update the replenish request status to "rejected"
        request.setStatus("rejected");
        saveReplenishRequests(); // Save updated replenish requests to the file
    }


    private static boolean isValidReplenishRequestSelection(String input) {
        try {
            int requestID = Integer.parseInt(input);
            return getReplenishRequests().stream().anyMatch(request -> request.getID() == requestID);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static StockReplenishRequest getReplenishRequestByID(int requestID) {
        return getReplenishRequests().stream()
                .filter(request -> request.getID() == requestID)
                .findFirst()
                .orElse(null);
    }

    // Static method to save all replenish requests to the CSV file
    public static void saveReplenishRequests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPLENISH_REQUESTS_FILE))) {
            bw.write("ID,StockID,IncomingStockLevel,Status");
            bw.newLine();
            for (StockReplenishRequest request : replenishRequests) {
                bw.write(String.join(",",
                        String.valueOf(request.getID()),
                        String.valueOf(request.getStockId()),
                        String.valueOf(request.getIncomingStockLevel()),
                        request.getStatus()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save replenish requests: " + e.getMessage());
        }
    }

    // Static method to load stocks from the CSV file
    public static void loadStocks() {
        File stockFile = new File(STOCKS_FILE);
        if (!stockFile.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(stockFile))) {
                bw.write("ID,MedicineName,StockLevel,LowStockAlertThreshold");
                bw.newLine();
            } catch (IOException e) {
                System.err.println("Error creating stocks file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(stockFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] stockDetails = line.split(",");
                if (stockDetails.length < 4) {
                    continue;
                }
                int id = Integer.parseInt(stockDetails[0]);
                String medicineName = stockDetails[1];
                int stockLevel = Integer.parseInt(stockDetails[2]);
                int lowStockAlertThreshold = Integer.parseInt(stockDetails[3]);
                Stock stock = new Stock(id, medicineName, stockLevel, lowStockAlertThreshold);
                stocks.add(stock);
            }
        } catch (IOException e) {
            System.err.println("Error reading stocks file: " + e.getMessage());
        }
    }

    public static void saveStocks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCKS_FILE))) {
            bw.write("ID,MedicineName,StockLevel,LowStockAlertThreshold");
            bw.newLine();

            for (Stock stock : stocks) {
                String stockLine = String.join(",",
                        String.valueOf(stock.getID()),
                        stock.getMedicineName(),
                        String.valueOf(stock.getStockLevel()),
                        String.valueOf(stock.getLowStockAlertThreshold())
                );
                bw.write(stockLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save stocks: " + e.getMessage());
        }
    }

    // Static method to load replenish requests from the CSV file
    public static void loadReplenishRequests() {
        File replenishFile = new File(REPLENISH_REQUESTS_FILE);
        if (!replenishFile.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(replenishFile))) {
                bw.write("ID,StockID,IncomingStockLevel,Status");
                bw.newLine();
            } catch (IOException e) {
                System.err.println("Error creating replenish requests file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(replenishFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] requestDetails = line.split(",");
                if (requestDetails.length < 4) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(requestDetails[0]);
                    int stockId = Integer.parseInt(requestDetails[1]);
                    int incomingStockLevel = Integer.parseInt(requestDetails[2]);
                    String status = requestDetails[3];
                    StockReplenishRequest request = new StockReplenishRequest(stockId, incomingStockLevel, status);
                    request.setID(id);
                    replenishRequests.add(request);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in replenish request entry: " + line);
                }
            }

            // Set the next available ID based on the highest existing ID
            nextReplenishRequestID = replenishRequests.stream()
                    .mapToInt(StockReplenishRequest::getID)
                    .max()
                    .orElse(0) + 1; // Increment the max ID by 1
        } catch (IOException e) {
            System.err.println("Error loading replenish requests file: " + e.getMessage());
        }
    }

    public static Stock getStockById(int stockId) {
        return stocks.stream()
                .filter(stock -> stock.getID() == stockId)
                .findFirst()
                .orElse(null); // Return null if the stock is not found
    }
}