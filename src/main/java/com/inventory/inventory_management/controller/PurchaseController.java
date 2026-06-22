package com.inventory.inventory_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management.dto.PurchaseRequest;
import com.inventory.inventory_management.dto.PurchaseResponse;
import com.inventory.inventory_management.service.PurchaseService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody PurchaseRequest request) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(request));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getPurchases(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }
}
