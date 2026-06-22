package com.inventory.inventory_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management.dto.InventoryLogResponse;
import com.inventory.inventory_management.dto.InventorySummaryResponse;
import com.inventory.inventory_management.dto.ProductResponse;
import com.inventory.inventory_management.service.InventoryService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final AccessControlService accessControlService;

    @GetMapping
    public ResponseEntity<InventorySummaryResponse> getSummary(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(inventoryService.getSummary());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStock(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(inventoryService.getLowStockProducts());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductResponse>> getOutOfStock(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(inventoryService.getOutOfStockProducts());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<InventoryLogResponse>> getLogs(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.ok(inventoryService.getInventoryLogs());
    }
}
