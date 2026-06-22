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
public class PurchaseResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime purchaseDate;
    private BigDecimal totalAmount;
    private List<PurchaseItemResponse> items;
}
