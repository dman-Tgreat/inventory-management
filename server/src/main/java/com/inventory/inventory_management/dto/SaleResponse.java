package com.inventory.inventory_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {
    private Long id;
    private Long userId;
    private String userName;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private BigDecimal profit;
    private List<SaleItemResponse> items;
}
