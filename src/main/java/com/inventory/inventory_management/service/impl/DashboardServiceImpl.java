package com.inventory.inventory_management.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.DashboardResponse;
import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.entity.SaleItem;
import com.inventory.inventory_management.repository.CategoryRepository;
import com.inventory.inventory_management.repository.ProductRepository;
import com.inventory.inventory_management.repository.PurchaseRepository;
import com.inventory.inventory_management.repository.SaleItemRepository;
import com.inventory.inventory_management.repository.SaleRepository;
import com.inventory.inventory_management.repository.SupplierRepository;
import com.inventory.inventory_management.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleItemRepository saleItemRepository;

    @Override
    public DashboardResponse getDashboard() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        BigDecimal todaysSales = saleRepository.findBySaleDateBetween(start, end).stream()
                .map(sale -> sale.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal todaysPurchases = purchaseRepository.findByPurchaseDateBetween(start, end).stream()
                .map(purchase -> purchase.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal revenue = saleRepository.findAll().stream()
                .map(sale -> sale.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal profit = calculateProfit(saleItemRepository.findAll());

        long lowStock = productRepository.findAll().stream().filter(this::isLowStock).count();

        return new DashboardResponse(
                productRepository.count(),
                categoryRepository.count(),
                supplierRepository.count(),
                todaysSales,
                todaysPurchases,
                lowStock,
                revenue,
                profit);
    }

    private BigDecimal calculateProfit(List<SaleItem> items) {
        return items.stream()
                .map(item -> item.getSellingPrice()
                        .subtract(item.getProduct().getCostPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isLowStock(Product product) {
        int quantity = product.getQuantity() == null ? 0 : product.getQuantity();
        int minimumStock = product.getMinimumStock() == null ? 0 : product.getMinimumStock();
        return quantity <= minimumStock;
    }
}
