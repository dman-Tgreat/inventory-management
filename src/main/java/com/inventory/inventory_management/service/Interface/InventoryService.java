package com.inventory.inventory_management.service.Interface;

import java.util.List;

import com.inventory.inventory_management.dto.InventoryLogResponse;
import com.inventory.inventory_management.dto.InventorySummaryResponse;
import com.inventory.inventory_management.dto.ProductResponse;

public interface InventoryService {
    InventorySummaryResponse getSummary();

    List<ProductResponse> getLowStockProducts();

    List<ProductResponse> getOutOfStockProducts();

    List<InventoryLogResponse> getInventoryLogs();
}
