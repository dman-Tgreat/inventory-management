package com.inventory.inventory_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private Long supplierId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private Integer minimumStock;
    private String barcode;
    private LocalDateTime createdAt;
}
