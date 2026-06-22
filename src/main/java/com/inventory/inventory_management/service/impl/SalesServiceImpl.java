package com.inventory.inventory_management.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory_management.dto.SaleItemRequest;
import com.inventory.inventory_management.dto.SaleItemResponse;
import com.inventory.inventory_management.dto.SaleRequest;
import com.inventory.inventory_management.dto.SaleResponse;
import com.inventory.inventory_management.entity.InventoryLog;
import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.entity.Sale;
import com.inventory.inventory_management.entity.SaleItem;
import com.inventory.inventory_management.entity.User;
import com.inventory.inventory_management.exception.BadRequestException;
import com.inventory.inventory_management.exception.ResourceNotFoundException;
import com.inventory.inventory_management.repository.InventoryLogRepository;
import com.inventory.inventory_management.repository.ProductRepository;
import com.inventory.inventory_management.repository.SaleItemRepository;
import com.inventory.inventory_management.repository.SaleRepository;
import com.inventory.inventory_management.repository.UserRepository;
import com.inventory.inventory_management.service.SalesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryLogRepository inventoryLogRepository;

    @Override
    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateStockBeforeSaving(request.getItems());

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setTotalAmount(BigDecimal.ZERO);
        sale = saleRepository.save(sale);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;
        List<SaleItem> savedItems = new ArrayList<>();

        for (SaleItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            int quantityBefore = defaultZero(product.getQuantity());
            int quantityAfter = quantityBefore - itemRequest.getQuantity();
            product.setQuantity(quantityAfter);
            productRepository.save(product);

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setSellingPrice(product.getSellingPrice());
            savedItems.add(saleItemRepository.save(item));

            InventoryLog log = new InventoryLog();
            log.setProduct(product);
            log.setType("SALE");
            log.setQuantityBefore(quantityBefore);
            log.setQuantityAfter(quantityAfter);
            log.setReason("Sale #" + sale.getId());
            inventoryLogRepository.save(log);

            BigDecimal quantity = BigDecimal.valueOf(itemRequest.getQuantity());
            total = total.add(product.getSellingPrice().multiply(quantity));
            profit = profit.add(product.getSellingPrice().subtract(product.getCostPrice()).multiply(quantity));
        }

        sale.setTotalAmount(total);
        Sale savedSale = saleRepository.save(sale);
        return toResponse(savedSale, savedItems, profit);
    }

    @Override
    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll().stream()
                .map(sale -> toResponse(sale, List.of(), BigDecimal.ZERO))
                .toList();
    }

    private void validateStockBeforeSaving(List<SaleItemRequest> items) {
        for (SaleItemRequest itemRequest : items) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            int quantity = defaultZero(product.getQuantity());
            if (quantity < itemRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }
        }
    }

    private SaleResponse toResponse(Sale sale, List<SaleItem> items, BigDecimal profit) {
        List<SaleItemResponse> itemResponses = items.stream()
                .map(item -> new SaleItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getSellingPrice(),
                        item.getSellingPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .toList();

        return new SaleResponse(
                sale.getId(),
                sale.getUser().getId(),
                sale.getUser().getName(),
                sale.getSaleDate(),
                sale.getTotalAmount(),
                profit,
                itemResponses);
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
