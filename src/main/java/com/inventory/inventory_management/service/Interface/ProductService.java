package com.inventory.inventory_management.service.Interface;

import java.util.List;

import com.inventory.inventory_management.dto.ProductRequest;
import com.inventory.inventory_management.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    ProductResponse getProduct(Long id);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> searchProducts(String keyword);

    List<ProductResponse> filterProducts(Long categoryId, Long supplierId, Boolean lowStock);
}
