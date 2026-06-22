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

import com.inventory.inventory_management.dto.SaleRequest;
import com.inventory.inventory_management.dto.SaleResponse;
import com.inventory.inventory_management.service.SalesService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {
    private final SalesService salesService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody SaleRequest request) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(salesService.createSale(request));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getSales(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(salesService.getAllSales());
    }
}
