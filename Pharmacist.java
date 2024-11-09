import java.util.List;

public class Pharmacist {
    private int ID;
    private final StockSystem stockSystem;

    // Constructor to initialize Pharmacist with StockSystem
    public Pharmacist(int ID, StockSystem stockSystem) {
        this.ID = ID;
        this.stockSystem = stockSystem;
    }

    // Getter and Setter for ID
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public void viewAppointmentOutcome() {
        // TODO: once appointment is included
    }
    
    public void updatePrescriptionStatus() {
        // TODO: once prescription is included
    }

    public Stock checkStock(int stockId) {
        Stock stock = stockSystem.getStockById(stockId);

        if (stock != null) {
            System.out.println("Stock for medicine " + stock.getMedicineName() + ": " + stock.getStockLevel());
        } else {
            System.out.println("No stock found with ID: " + stockId);
        }
        return stock;
    }

    // TODO: Let me know if you want pharmacist to top up ALL stocks regardless of stock level
    //  or to request only for those which are below the threshold
    public boolean submitReplenishRequest() {
        List<Stock> lowLevelStocks = stockSystem.getLowLevelStocks();

        if (!lowLevelStocks.isEmpty()) {
            for (Stock stock : lowLevelStocks) {
                StockReplenishRequest replenishRequest = new StockReplenishRequest();
                replenishRequest.setStockId(stock.getID());
                replenishRequest.setIncomingStockLevel(100); // Example value for replenishment
                replenishRequest.setStatus("Pending");
                stockSystem.createReplenishRequest(replenishRequest);
                System.out.println("Replenish request submitted for medicine: " + stock.getMedicineName());
            }
            return true;
        } else {
            System.out.println("No stocks are below the threshold level.");
            return false;
        }
    }
}
