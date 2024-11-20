package Repositories;

import Enums.ReplenishStatus;
import Models.StockReplenishRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentRepository implements IReplenishmentRepository, LoadandSaveInterface<StockReplenishRequest> {
    private static final String REPLENISH_REQUESTS_FILE = "data/replenish_requests.csv";

    @Override
    public List<StockReplenishRequest> getAllRequests() {
        return loadData(); // Delegate to loadData()
    }

    @Override
    public List<StockReplenishRequest> getRequestsByStatus(ReplenishStatus status) {
        return getAllRequests().stream()
                .filter(request -> request.getStatus() == status)
                .toList();
    }

    @Override
    public void saveAllRequests(List<StockReplenishRequest> requests) {
        saveData(requests); // Delegate to saveData()
    }

    @Override
    public StockReplenishRequest findRequestById(int requestId) {
        return getAllRequests().stream()
                .filter(request -> request.getID() == requestId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<StockReplenishRequest> loadData() {
        List<StockReplenishRequest> requests = new ArrayList<>();
        File file = new File(REPLENISH_REQUESTS_FILE);

        if (!file.exists()) {
            System.err.println("File not found: " + REPLENISH_REQUESTS_FILE);
            return requests; // Return an empty list if the file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip the header

            while ((line = br.readLine()) != null) {
                StockReplenishRequest request = parseRequest(line);
                if (request != null) {
                    requests.add(request);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading replenish requests: " + e.getMessage());
        }

        return requests;
    }

    @Override
    public void saveData(List<StockReplenishRequest> requests) {
        File file = new File(REPLENISH_REQUESTS_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Write the CSV header
            bw.write("ID,StockID,IncomingStockLevel,Status");
            bw.newLine();

            // Write each replenish request's data
            for (StockReplenishRequest request : requests) {
                bw.write(formatRequest(request));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save replenish requests: " + e.getMessage());
        }
    }

    private StockReplenishRequest parseRequest(String line) {
        String[] parts = line.split(",", 4); // Split into 4 parts for all fields
        if (parts.length < 4) {
            return null; // Invalid line
        }

        try {
            int id = Integer.parseInt(parts[0]);
            int stockId = Integer.parseInt(parts[1]);
            int incomingStockLevel = Integer.parseInt(parts[2]);
            ReplenishStatus status = ReplenishStatus.valueOf(parts[3].toUpperCase());

            StockReplenishRequest request = new StockReplenishRequest(stockId, incomingStockLevel, status);
            request.setID(id);
            return request;
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing request line: " + line + ". Error: " + e.getMessage());
        }

        return null;
    }

    private String formatRequest(StockReplenishRequest request) {
        return String.join(",",
                String.valueOf(request.getID()),
                String.valueOf(request.getStockId()),
                String.valueOf(request.getIncomingStockLevel()),
                request.getStatus().toString()
        );
    }
}
