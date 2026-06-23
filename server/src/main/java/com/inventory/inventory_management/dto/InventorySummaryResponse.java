package com.inventory.inventory_management.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySummaryResponse {
    private long totalProducts;
    private int currentStock;
    private long lowStockProducts;
    private long outOfStockProducts;
    private BigDecimal inventoryValue;
}
