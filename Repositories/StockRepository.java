package Repositories;

import Models.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockRepository implements IStockRepository {
    private static final String STOCKS_FILE = "data/stocks.csv";

    @Override
    public void saveAllStocks(List<Stock> stocks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCKS_FILE))) {
            // Write the CSV header
            bw.write("ID,MedicineName,StockLevel,LowStockAlertThreshold");
            bw.newLine();
    
            // Write each stock's data as a new line in the CSV
            for (Stock stock : stocks) {
                String line = String.join(",",
                        String.valueOf(stock.getID()), // ID
                        stock.getMedicineName(),       // Medicine Name
                        String.valueOf(stock.getStockLevel()), // Stock Level
                        String.valueOf(stock.getLowStockAlertThreshold()) // Threshold
                );
                bw.write(line);
                bw.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            System.err.println("Failed to save stocks to file: " + e.getMessage());
        }
    }
    

    @Override
    public Stock findById(int stockId) {
        return getAllStocks().stream()
                .filter(stock -> stock.getID() == stockId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Stock> getAllStocks() {
    List<Stock> stocks = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(STOCKS_FILE))) {
        String line;
        br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] details = line.split(",");
            int id = Integer.parseInt(details[0]);
            String medicineName = details[1];
            int stockLevel = Integer.parseInt(details[2]);
            int lowStockThreshold = Integer.parseInt(details[3]);

            Stock stock = new Stock(id, medicineName, stockLevel, lowStockThreshold);
            stocks.add(stock);
        }
    } catch (IOException e) {
        System.err.println("Error loading stocks: " + e.getMessage());
    }
    return stocks;
}

}
