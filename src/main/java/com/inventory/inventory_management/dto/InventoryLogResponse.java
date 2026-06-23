package com.inventory.inventory_management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String type;
    private Integer quantityBefore;
    private Integer quantityAfter;
    private String reason;
    private LocalDateTime createdAt;
}
