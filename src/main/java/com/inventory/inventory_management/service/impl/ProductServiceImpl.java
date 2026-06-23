package com.inventory.inventory_management.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory_management.dto.ProductRequest;
import com.inventory.inventory_management.dto.ProductResponse;
import com.inventory.inventory_management.entity.Category;
import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.entity.Supplier;
import com.inventory.inventory_management.exception.BadRequestException;
import com.inventory.inventory_management.exception.ProductNotFoundException;
import com.inventory.inventory_management.exception.ResourceNotFoundException;
import com.inventory.inventory_management.mapper.ProductMapper;
import com.inventory.inventory_management.repository.CategoryRepository;
import com.inventory.inventory_management.repository.ProductRepository;
import com.inventory.inventory_management.repository.SupplierRepository;
import com.inventory.inventory_management.service.Interface.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        validatePrices(request.getCostPrice(), request.getSellingPrice());
        if (productRepository.existsBySku(request.getSku())) {
            throw new BadRequestException("SKU already exists: " + request.getSku());
        }

        Product product = new Product();
        applyRequest(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductEntity(id);
        validatePrices(request.getCostPrice(), request.getSellingPrice());
        if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new BadRequestException("SKU already exists: " + request.getSku());
        }

        applyRequest(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductEntity(id);
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return productMapper.toResponse(getProductEntity(id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        String search = keyword == null ? "" : keyword.trim();
        return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(search, search)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> filterProducts(Long categoryId, Long supplierId, Boolean lowStock) {
        return productRepository.filterProducts(categoryId, supplierId, lowStock)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    private void applyRequest(Product product, ProductRequest request) {
        Category category = request.getCategoryId() == null ? null
                : categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Supplier supplier = request.getSupplierId() == null ? null
                : supplierRepository.findById(request.getSupplierId())
                        .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setQuantity(defaultZero(request.getQuantity()));
        product.setMinimumStock(defaultZero(request.getMinimumStock()));
        product.setBarcode(request.getBarcode());
    }

    private Product getProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private void validatePrices(BigDecimal costPrice, BigDecimal sellingPrice) {
        if (costPrice == null || sellingPrice == null) {
            throw new BadRequestException("Cost price and selling price are required");
        }
        if (costPrice.compareTo(BigDecimal.ZERO) < 0 || sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Prices cannot be negative");
        }
        if (sellingPrice.compareTo(costPrice) < 0) {
            throw new BadRequestException("Selling price cannot be lower than cost price");
        }
    }

    private Integer defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
