package Systems;

import Models.Stock;
import java.util.List;


public interface IStockSystem {
    List<Stock> getAllStocks();
    Stock findStockByMedicineName(String medicationName);
    void createReplenishRequest(int stockID, int quantity);
}
