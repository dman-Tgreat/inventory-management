package com.inventory.inventory_management.service.Interface;

import java.util.List;

import com.inventory.inventory_management.dto.CategoryRequest;
import com.inventory.inventory_management.dto.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();
}
