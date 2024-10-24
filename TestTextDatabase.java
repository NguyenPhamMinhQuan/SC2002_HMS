public class TestTextDatabase {
    public static void main(String[] args) {
        // Create a new StockSystem
        StockSystem stockSystem = new StockSystem();

        // Add some stocks
        Stock stock1 = new Stock(1, "Aspirin", 100, 20);
        Stock stock2 = new Stock(2, "Ibuprofen", 50, 10);
        Stock stock3 = new Stock(3, "Paracetamol", 200, 50);

        stockSystem.createStock(stock1);
        stockSystem.createStock(stock2);
        stockSystem.createStock(stock3);

        // Add a stock replenish request
        StockReplenishRequest request1 = new StockReplenishRequest(1, 1, 50, "Pending");
        stockSystem.createReplenishRequest(request1);

        // Display all stocks
        System.out.println("All Stocks:");
        for (Stock stock : stockSystem.getStocks()) {
            System.out.println(stock);
        }

        // Display stocks that are at low level
        System.out.println("\nLow Level Stocks:");
        for (Stock stock : stockSystem.getLowLevelStocks()) {
            System.out.println(stock);
        }

        // Update a stock
        stock1.setStockLevel(15); // Set new stock level to be below threshold
        stockSystem.updateStock(stock1);

        System.out.println("\nUpdated Stock:");
        System.out.println(stockSystem.getStockById(1));

        // Delete a stock
        stockSystem.deleteStock(stock2);
        System.out.println("\nStocks after deletion:");
        for (Stock stock : stockSystem.getStocks()) {
            System.out.println(stock);
        }

        // Display all replenish requests
        System.out.println("\nStock Replenish Requests:");
        for (StockReplenishRequest request : stockSystem.getReplenishRequests()) {
            System.out.println(request);
        }

        // Update a replenish request
        request1.setStatus("Completed");
        stockSystem.updateReplenishRequest(request1);

        System.out.println("\nUpdated Stock Replenish Request:");
        System.out.println(stockSystem.getReplenishRequests().get(0));
    }
}
