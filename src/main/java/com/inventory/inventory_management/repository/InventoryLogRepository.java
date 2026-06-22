package com.inventory.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management.entity.InventoryLog;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
}
