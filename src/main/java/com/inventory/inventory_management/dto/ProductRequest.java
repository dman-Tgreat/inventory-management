package com.inventory.inventory_management.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private Long categoryId;
    private Long supplierId;

    @NotBlank
    private String sku;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @PositiveOrZero
    private BigDecimal costPrice;

    @NotNull
    @PositiveOrZero
    private BigDecimal sellingPrice;

    @PositiveOrZero
    private Integer quantity;

    @PositiveOrZero
    private Integer minimumStock;

    private String barcode;
}
