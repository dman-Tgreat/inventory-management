package com.inventory.inventory_management.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private long totalProducts;
    private long totalCategories;
    private long totalSuppliers;
    private BigDecimal todaysSales;
    private BigDecimal todaysPurchases;
    private long lowStock;
    private BigDecimal revenue;
    private BigDecimal profit;
}
