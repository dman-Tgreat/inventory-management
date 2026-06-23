package com.inventory.inventory_management.mapper;

import org.springframework.stereotype.Component;

import com.inventory.inventory_management.dto.ProductResponse;
import com.inventory.inventory_management.entity.Product;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        Long categoryId = product.getCategory() == null ? null : product.getCategory().getId();
        Long supplierId = product.getSupplier() == null ? null : product.getSupplier().getId();

        return new ProductResponse(
                product.getId(),
                categoryId,
                supplierId,
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCostPrice(),
                product.getSellingPrice(),
                product.getQuantity(),
                product.getMinimumStock(),
                product.getBarcode(),
                product.getCreatedAt());
    }
}
