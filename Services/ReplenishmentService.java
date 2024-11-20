package Services;

import Enums.ReplenishStatus;
import Models.StockReplenishRequest;
import Models.Stock;
import Repositories.IReplenishmentRepository;
import Repositories.IStockRepository;

import java.util.List;

public class ReplenishmentService {
    private final IReplenishmentRepository replenishRepository;
    private final IStockRepository stockRepository;
    private int nextReplenishRequestID;

    public ReplenishmentService(IReplenishmentRepository replenishRepository, IStockRepository stockRepository) {
        this.replenishRepository = replenishRepository;
        this.stockRepository = stockRepository;
        initializeNextID();
    }

    private void initializeNextID() {
        this.nextReplenishRequestID = replenishRepository.getAllRequests().stream()
                .mapToInt(StockReplenishRequest::getID)
                .max()
                .orElse(0) + 1;
    }

    public List<StockReplenishRequest> getPendingRequests() {
        return replenishRepository.getRequestsByStatus(ReplenishStatus.PENDING);
    }

    public StockReplenishRequest findRequestById(int requestId) {
        return replenishRepository.findRequestById(requestId);
    }

    public void saveRequests(List<StockReplenishRequest> requests) {
        replenishRepository.saveAllRequests(requests);
    }

    public StockReplenishRequest createRequest(int stockId, int incomingStockLevel) {
        StockReplenishRequest request = new StockReplenishRequest(stockId, incomingStockLevel, ReplenishStatus.PENDING);
        request.setID(nextReplenishRequestID++);
        List<StockReplenishRequest> requests = replenishRepository.getAllRequests();
        requests.add(request);
        saveRequests(requests);
        return request;
    }

    public void approveRequest(StockReplenishRequest request) {
        Stock stock = stockRepository.findById(request.getStockId());
        if (stock == null) {
            System.out.println("Stock not found for Stock ID: " + request.getStockId());
            return;
        }

        // Update stock level with the incoming stock level
        stock.setStockLevel(stock.getStockLevel() + request.getIncomingStockLevel());
        stockRepository.saveAllStocks(stockRepository.getAllStocks());

        // Update the request status to APPROVED
        request.setStatus(ReplenishStatus.APPROVED);
        replenishRepository.saveAllRequests(replenishRepository.getAllRequests());
    }

    public void rejectRequest(StockReplenishRequest request) {
        // Update the request status to REJECTED
        request.setStatus(ReplenishStatus.REJECTED);
        replenishRepository.saveAllRequests(replenishRepository.getAllRequests());
    }

    public StockReplenishRequest updateReplenishRequest(StockReplenishRequest updatedRequest) {
        List<StockReplenishRequest> requests = replenishRepository.getAllRequests();

        for (StockReplenishRequest request : requests) {
            if (request.getID() == updatedRequest.getID()) {
                request.setStockId(updatedRequest.getStockId());
                request.setIncomingStockLevel(updatedRequest.getIncomingStockLevel());
                request.setStatus(updatedRequest.getStatus());
                replenishRepository.saveAllRequests(requests);
                return request;
            }
        }
        return null;
    }
}
