package com.inventory.inventory_management.service;

import java.util.List;

import com.inventory.inventory_management.dto.PurchaseRequest;
import com.inventory.inventory_management.dto.PurchaseResponse;

public interface PurchaseService {
    PurchaseResponse createPurchase(PurchaseRequest request);

    List<PurchaseResponse> getAllPurchases();
}
