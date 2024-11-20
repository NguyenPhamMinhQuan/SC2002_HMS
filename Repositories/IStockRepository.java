package Repositories;

import Models.Stock;

import java.util.List;

public interface IStockRepository {
    List<Stock> getAllStocks();
    void saveAllStocks(List<Stock> stocks);
    Stock findById(int stockId);
}
