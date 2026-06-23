package com.inventory.inventory_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventory.inventory_management.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    List<Product> findByQuantityLessThanEqual(Integer quantity);

    List<Product> findByQuantity(Integer quantity);

    @Query("""
            select p from Product p
            where (:categoryId is null or p.category.id = :categoryId)
              and (:supplierId is null or p.supplier.id = :supplierId)
              and (:lowStock is null or (:lowStock = true and p.quantity <= p.minimumStock))
            """)
    List<Product> filterProducts(
            @Param("categoryId") Long categoryId,
            @Param("supplierId") Long supplierId,
            @Param("lowStock") Boolean lowStock);
}
