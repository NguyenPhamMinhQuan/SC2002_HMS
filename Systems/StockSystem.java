package Systems;

import Enums.ReplenishStatus;
import Models.Stock;
import Models.StockReplenishRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The StockSystem class handles the management of stocks and replenish requests in the system.
 * It provides methods to load, save, and manipulate stock data, as well as create and update replenish requests.
 */
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

    /**
     * Returns the list of all stocks in the system.
     *
     * @return a list of all stocks
     */
    public static List<Stock> getStocks() {
        return stocks;
    }

    /**
     * Returns the list of stocks with a stock level below or equal to their low stock alert threshold.
     *
     * @return a list of low-level stocks
     */
    public static List<Stock> getLowLevelStocks() {
        return stocks.stream()
                .filter(stock -> stock.getStockLevel() <= stock.getLowStockAlertThreshold())
                .collect(Collectors.toList());
    }

    /**
     * Creates a replenish request for a stock.
     * If a pending request for the same stock already exists, it updates the existing request with the incoming stock level.
     *
     * @param stockRequest the replenish request to be created
     * @return the created or updated replenish request
     */
    public static StockReplenishRequest createReplenishRequest(StockReplenishRequest stockRequest) {
        stockRequest.setID(nextReplenishRequestID++);

        for (StockReplenishRequest existingRequest : replenishRequests) {
            if (existingRequest.getStockId() == stockRequest.getStockId() &&
                    existingRequest.getStatus() == ReplenishStatus.PENDING) {
                existingRequest.setIncomingStockLevel(stockRequest.getIncomingStockLevel());
                saveReplenishRequests();
                return existingRequest;
            }
        }

        replenishRequests.add(stockRequest);
        saveReplenishRequests();
        return stockRequest;
    }

    /**
     * Updates an existing replenish request.
     *
     * @param request the replenish request with updated details
     * @return the updated replenish request, or null if the request doesn't exist
     */
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

    /**
     * Returns a list of all replenish requests that are currently pending.
     *
     * @return a list of pending replenish requests
     */
    public static List<StockReplenishRequest> getReplenishRequests() {
        return replenishRequests.stream()
                .filter(x -> x.getStatus() == ReplenishStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Displays low-level stock items and allows the user to create a replenish request for them.
     * Prompts the user for stock selection and quantity to replenish.
     */
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
                ReplenishStatus.PENDING         // Initial status is "pending"
        );

        // Add the replenish request to the system
        StockSystem.createReplenishRequest(replenishRequest);
        System.out.println("Replenishment request created successfully for " + selectedStock.getMedicineName() + " with quantity " + replenishQuantity + ".");
    }

    /**
     * Helper function to validate the stock selection input.
     *
     * @param input the user's input
     * @param size the number of available stocks to choose from
     * @return true if the input is a valid stock selection
     */
    private static boolean isValidStockSelection(String input, int size) {
        try {
            int index = Integer.parseInt(input);
            return index > 0 && index <= size;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Prints all the stocks in a table format.
     */
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

    /**
     * Displays all pending replenish requests in a table format.
     */
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

    /**
     * Handles the approval or rejection of a replenish request
     * Prompts the admin to select a request and approve or reject it.
     */
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

    /**
     * Approves the replenish request, updates stock levels, and changes the request status to approved.
     *
     * @param request the replenish request to approve
     */
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
        request.setStatus(ReplenishStatus.APPROVED);
        saveReplenishRequests(); // Save updated replenish requests to the file
    }

    /**
     * Rejects the replenish request and sets the request status to rejected.
     *
     * @param request the replenish request to reject
     */
    private static void rejectReplenishRequest(StockReplenishRequest request) {
        // Update the replenish request status to "rejected"
        request.setStatus(ReplenishStatus.REJECTED);
        saveReplenishRequests(); // Save updated replenish requests to the file
    }

    /**
     * Validates whether the selected replenish request ID is valid.
     *
     * @param input the user input for request ID
     * @return true if the input corresponds to a valid replenish request ID, otherwise false
     */
    private static boolean isValidReplenishRequestSelection(String input) {
        try {
            int requestID = Integer.parseInt(input);
            return getReplenishRequests().stream().anyMatch(request -> request.getID() == requestID);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Retrieves the replenish request by its ID.
     *
     * @param requestID the ID of the replenish request
     * @return the replenish request if found, or null if not found
     */
    private static StockReplenishRequest getReplenishRequestByID(int requestID) {
        return getReplenishRequests().stream()
                .filter(request -> request.getID() == requestID)
                .findFirst()
                .orElse(null);
    }


    /**
     * Saves the list of replenish requests to the storage file.
     */
    public static void saveReplenishRequests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPLENISH_REQUESTS_FILE))) {
            bw.write("ID,StockID,IncomingStockLevel,Status");
            bw.newLine();
            for (StockReplenishRequest request : replenishRequests) {
                bw.write(String.join(",",
                        String.valueOf(request.getID()),
                        String.valueOf(request.getStockId()),
                        String.valueOf(request.getIncomingStockLevel()),
                        request.getStatus().toString()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save replenish requests: " + e.getMessage());
        }
    }

    /**
     * Loads stock data from the storage file into memory.
     */
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

    /**
     * Saves the list of stocks to the storage file.
     */
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

    /**
     *  Loads replenish requests from the storage file into memory.
     */
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
                    ReplenishStatus status = ReplenishStatus.valueOf(requestDetails[3].toUpperCase());
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

    /**
     * Retrieves the stock by its ID.
     *
     * @param stockId the ID of the stock
     * @return the stock object if found or null if not found
     */
    public static Stock getStockById(int stockId) {
        return stocks.stream()
                .filter(stock -> stock.getID() == stockId)
                .findFirst()
                .orElse(null); // Return null if the stock is not found
    }
}
