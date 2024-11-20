package Services;

import Models.Stock;
import Repositories.IStockRepository;

import java.util.List;

public class StockService {
    private final IStockRepository stockRepository;

    public StockService(IStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.getAllStocks();
    }

    public List<Stock> getLowLevelStocks() {
        return getAllStocks().stream()
                .filter(stock -> stock.getStockLevel() <= stock.getLowStockAlertThreshold())
                .toList();
    }

    public Stock findStockById(int stockId) {
        return stockRepository.findById(stockId);
    }

    public void saveStocks(List<Stock> stocks) {
        stockRepository.saveAllStocks(stocks);
    }
}
