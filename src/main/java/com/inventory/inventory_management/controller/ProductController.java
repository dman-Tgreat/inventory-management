package com.inventory.inventory_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management.dto.ProductRequest;
import com.inventory.inventory_management.dto.ProductResponse;
import com.inventory.inventory_management.service.ProductService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody ProductRequest request) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestHeader("X-Role") String role,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader("X-Role") String role, @PathVariable Long id) {
        accessControlService.requireAdmin(role);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@RequestHeader("X-Role") String role, @PathVariable Long id) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestHeader("X-Role") String role,
            @RequestParam String keyword) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> filterProducts(
            @RequestHeader("X-Role") String role,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Boolean lowStock) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(productService.filterProducts(categoryId, supplierId, lowStock));
    }
}
