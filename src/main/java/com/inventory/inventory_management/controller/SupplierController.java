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

import com.inventory.inventory_management.dto.SupplierRequest;
import com.inventory.inventory_management.dto.SupplierResponse;
import com.inventory.inventory_management.service.Interface.SupplierService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody SupplierRequest request) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(request));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getSuppliers(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }
}
