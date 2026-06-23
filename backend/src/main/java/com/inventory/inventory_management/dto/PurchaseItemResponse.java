package com.inventory.inventory_management.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitCost;
    private BigDecimal lineTotal;
}
