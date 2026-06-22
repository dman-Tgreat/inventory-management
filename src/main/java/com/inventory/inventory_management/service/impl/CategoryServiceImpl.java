package com.inventory.inventory_management.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.CategoryRequest;
import com.inventory.inventory_management.dto.CategoryResponse;
import com.inventory.inventory_management.entity.Category;
import com.inventory.inventory_management.repository.CategoryRepository;
import com.inventory.inventory_management.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
