package com.inventory.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management.entity.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findBySaleSaleDateBetween(LocalDateTime start, LocalDateTime end);
}
