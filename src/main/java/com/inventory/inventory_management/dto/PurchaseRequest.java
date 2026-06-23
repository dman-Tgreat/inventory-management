package com.inventory.inventory_management.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    @NotNull
    private Long supplierId;

    @Valid
    @NotEmpty
    private List<PurchaseItemRequest> items;
}
