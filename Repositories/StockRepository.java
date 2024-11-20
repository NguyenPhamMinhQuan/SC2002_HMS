package HMS.Repositories;

import HMS.Models.Stock;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockRepository implements LoadandSaveInterface<Stock> {

    private static final String STOCKS_FILE = "data/stocks.csv";

    @Override
    public List<Stock> loadData() {
        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STOCKS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                int id = Integer.parseInt(details[0]);
                String medicineName = details[1];
                int stockLevel = Integer.parseInt(details[2]);
                int lowStockAlertThreshold = Integer.parseInt(details[3]);

                Stock stock = new Stock(id, medicineName, stockLevel, lowStockAlertThreshold);
                stocks.add(stock);
            }
        } catch (IOException e) {
            System.err.println("Error loading stocks: " + e.getMessage());
        }
        return stocks;
    }

    @Override
    public void saveData(List<Stock> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCKS_FILE))) {
            bw.write("ID,MedicineName,StockLevel,LowStockAlertThreshold");
            bw.newLine();
            for (Stock stock : data) {
                bw.write(String.join(",",
                        String.valueOf(stock.getID()),
                        stock.getMedicineName(),
                        String.valueOf(stock.getStockLevel()),
                        String.valueOf(stock.getLowStockAlertThreshold())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving stocks: " + e.getMessage());
        }
    }
}
