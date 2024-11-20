package Repositories;

import Enums.ReplenishStatus;
import Models.StockReplenishRequest;

import java.util.List;

public interface IReplenishmentRepository {
    List<StockReplenishRequest> getAllRequests();
    List<StockReplenishRequest> getRequestsByStatus(ReplenishStatus status);
    void saveAllRequests(List<StockReplenishRequest> requests);
    StockReplenishRequest findRequestById(int requestId);
}