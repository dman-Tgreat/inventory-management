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

import com.inventory.inventory_management.dto.CategoryRequest;
import com.inventory.inventory_management.dto.CategoryResponse;
import com.inventory.inventory_management.service.CategoryService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody CategoryRequest request) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdminOrEmployee(role);
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
