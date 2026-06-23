package com.inventory.inventory_management.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.InventoryLogResponse;
import com.inventory.inventory_management.dto.InventorySummaryResponse;
import com.inventory.inventory_management.dto.ProductResponse;
import com.inventory.inventory_management.entity.InventoryLog;
import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.mapper.ProductMapper;
import com.inventory.inventory_management.repository.InventoryLogRepository;
import com.inventory.inventory_management.repository.ProductRepository;
import com.inventory.inventory_management.service.Interface.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final ProductMapper productMapper;

    @Override
    public InventorySummaryResponse getSummary() {
        List<Product> products = productRepository.findAll();
        int currentStock = products.stream().mapToInt(product -> defaultZero(product.getQuantity())).sum();
        long lowStock = products.stream().filter(this::isLowStock).count();
        long outOfStock = products.stream().filter(product -> defaultZero(product.getQuantity()) == 0).count();
        BigDecimal inventoryValue = products.stream()
                .map(product -> product.getCostPrice().multiply(BigDecimal.valueOf(defaultZero(product.getQuantity()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventorySummaryResponse(products.size(), currentStock, lowStock, outOfStock, inventoryValue);
    }

    @Override
    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(this::isLowStock)
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getOutOfStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> defaultZero(product.getQuantity()) == 0)
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<InventoryLogResponse> getInventoryLogs() {
        return inventoryLogRepository.findAll().stream().map(this::toResponse).toList();
    }

    private InventoryLogResponse toResponse(InventoryLog log) {
        Product product = log.getProduct();
        return new InventoryLogResponse(
                log.getId(),
                product == null ? null : product.getId(),
                product == null ? null : product.getName(),
                log.getType(),
                log.getQuantityBefore(),
                log.getQuantityAfter(),
                log.getReason(),
                log.getCreatedAt());
    }

    private boolean isLowStock(Product product) {
        return defaultZero(product.getQuantity()) <= defaultZero(product.getMinimumStock());
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
