package Repositories;

import Enums.ReplenishStatus;
import Models.StockReplenishRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ReplenishmentRepository implements IReplenishmentRepository {
    private static final String REPLENISH_REQUESTS_FILE = "data/replenish_requests.csv";

    @Override
    public List<StockReplenishRequest> getAllRequests() {
        List<StockReplenishRequest> requests = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(REPLENISH_REQUESTS_FILE))) {
            String line;
            br.readLine(); // Skip the header line
    
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 4) {
                    System.err.println("Invalid line in replenish requests file: " + line);
                    continue; // Skip invalid lines
                }
    
                try {
                    int id = Integer.parseInt(details[0]);                      // ID
                    int stockId = Integer.parseInt(details[1]);                 // Stock ID
                    int incomingStockLevel = Integer.parseInt(details[2]);      // Incoming Stock Level
                    ReplenishStatus status = ReplenishStatus.valueOf(details[3].toUpperCase()); // Status
    
                    // Create and add the StockReplenishRequest object to the list
                    StockReplenishRequest request = new StockReplenishRequest(stockId, incomingStockLevel, status);
                    request.setID(id);
                    requests.add(request);
    
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Replenish requests file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading replenish requests file: " + e.getMessage());
        }
    
        return requests;
    }
    
    @Override
    public List<StockReplenishRequest> getRequestsByStatus(ReplenishStatus status) {
        return getAllRequests().stream()
                .filter(request -> request.getStatus() == status)
                .toList();
    }

    @Override
    public void saveAllRequests(List<StockReplenishRequest> requests) {
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPLENISH_REQUESTS_FILE))) {
            // Write the CSV header
            bw.write("ID,StockID,IncomingStockLevel,Status");
            bw.newLine();
    
            // Write each replenish request's data as a new line in the CSV
            for (StockReplenishRequest request : requests) {
                String line = String.join(",",
                        String.valueOf(request.getID()),                     // Request ID
                        String.valueOf(request.getStockId()),                // Stock ID
                        String.valueOf(request.getIncomingStockLevel()),     // Incoming Stock Level
                        request.getStatus().toString()                      // Status
                );
                bw.write(line);
                bw.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            System.err.println("Failed to save replenish requests to file: " + e.getMessage());
        }
    }
    

    @Override
    public StockReplenishRequest findRequestById(int requestId) {
        return getAllRequests().stream()
                .filter(request -> request.getID() == requestId)
                .findFirst()
                .orElse(null);
    }
}
