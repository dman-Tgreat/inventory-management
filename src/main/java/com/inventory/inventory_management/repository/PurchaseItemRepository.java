package com.inventory.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management.entity.PurchaseItem;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
