package com.inventory.inventory_management.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory_management.dto.PurchaseItemRequest;
import com.inventory.inventory_management.dto.PurchaseItemResponse;
import com.inventory.inventory_management.dto.PurchaseRequest;
import com.inventory.inventory_management.dto.PurchaseResponse;
import com.inventory.inventory_management.entity.InventoryLog;
import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.entity.Purchase;
import com.inventory.inventory_management.entity.PurchaseItem;
import com.inventory.inventory_management.entity.Supplier;
import com.inventory.inventory_management.exception.BadRequestException;
import com.inventory.inventory_management.exception.ResourceNotFoundException;
import com.inventory.inventory_management.repository.InventoryLogRepository;
import com.inventory.inventory_management.repository.ProductRepository;
import com.inventory.inventory_management.repository.PurchaseItemRepository;
import com.inventory.inventory_management.repository.PurchaseRepository;
import com.inventory.inventory_management.repository.SupplierRepository;
import com.inventory.inventory_management.service.Interface.PurchaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryLogRepository inventoryLogRepository;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setTotalAmount(BigDecimal.ZERO);
        purchase = purchaseRepository.save(purchase);

        BigDecimal total = BigDecimal.ZERO;
        List<PurchaseItem> savedItems = new ArrayList<>();

        for (PurchaseItemRequest itemRequest : request.getItems()) {
            validatePurchaseItem(itemRequest);
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            int quantityBefore = defaultZero(product.getQuantity());
            int quantityAfter = quantityBefore + itemRequest.getQuantity();
            product.setQuantity(quantityAfter);
            product.setCostPrice(itemRequest.getUnitCost());
            productRepository.save(product);

            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitCost(itemRequest.getUnitCost());
            savedItems.add(purchaseItemRepository.save(item));

            InventoryLog log = new InventoryLog();
            log.setProduct(product);
            log.setType("PURCHASE");
            log.setQuantityBefore(quantityBefore);
            log.setQuantityAfter(quantityAfter);
            log.setReason("Purchase #" + purchase.getId());
            inventoryLogRepository.save(log);

            total = total.add(itemRequest.getUnitCost().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        purchase.setTotalAmount(total);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return toResponse(savedPurchase, savedItems);
    }

    @Override
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(purchase -> toResponse(purchase, List.of()))
                .toList();
    }

    private void validatePurchaseItem(PurchaseItemRequest itemRequest) {
        if (itemRequest.getUnitCost() == null || itemRequest.getUnitCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Purchase item unit cost cannot be negative");
        }
    }

    private PurchaseResponse toResponse(Purchase purchase, List<PurchaseItem> items) {
        List<PurchaseItemResponse> itemResponses = items.stream()
                .map(item -> new PurchaseItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitCost(),
                        item.getUnitCost().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .toList();

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getSupplier().getId(),
                purchase.getSupplier().getName(),
                purchase.getPurchaseDate(),
                purchase.getTotalAmount(),
                itemResponses);
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
