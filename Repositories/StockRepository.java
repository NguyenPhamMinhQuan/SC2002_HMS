package Repositories;

import Models.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockRepository implements IStockRepository, LoadandSaveInterface<Stock> {
    private static final String FILE_PATH = "data/stocks.csv";

    @Override
    public List<Stock> getAllStocks() {
        return loadData(); // Delegate to loadData()
    }

    @Override
    public Stock findById(int stockId) {
        return loadData().stream()
                .filter(stock -> stock.getID() == stockId)
                .findFirst()
                .orElse(null); // Return null if not found
    }

    @Override
    public void saveAllStocks(List<Stock> stocks) {
        saveData(stocks); // Delegate to saveData()
    }

    @Override
    public List<Stock> loadData() {
        List<Stock> stocks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.err.println("File not found: " + FILE_PATH);
            return stocks; // Return empty list if file is missing
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                Stock stock = parseStock(line);
                if (stock != null) {
                    stocks.add(stock);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stocks: " + e.getMessage());
        }

        return stocks;
    }

    @Override
    public void saveData(List<Stock> stocks) {
        File file = new File(FILE_PATH);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Write the header
            bw.write("StockID,MedicineName,StockLevel,LowStockAlertThreshold");
            bw.newLine();

            // Write each stock as a line in the file
            for (Stock stock : stocks) {
                bw.write(formatStock(stock));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving stocks: " + e.getMessage());
        }
    }

    private Stock parseStock(String line) {
        String[] parts = line.split(",", 4); // Split into 4 parts for all fields
        if (parts.length < 4) {
            return null;
        }

        try {
            int stockID = Integer.parseInt(parts[0]);
            String medicineName = parts[1];
            int stockLevel = Integer.parseInt(parts[2]);
            int lowStockAlertThreshold = Integer.parseInt(parts[3]);

            return new Stock(stockID, medicineName, stockLevel, lowStockAlertThreshold);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("Error parsing stock line: " + line + ". Error: " + e.getMessage());
        }

        return null;
    }

    private String formatStock(Stock stock) {
        return String.join(",",
                String.valueOf(stock.getID()),
                stock.getMedicineName(),
                String.valueOf(stock.getStockLevel()),
                String.valueOf(stock.getLowStockAlertThreshold())
        );
    }
}
