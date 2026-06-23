package com.inventory.inventory_management.service.Interface;

import java.util.List;

import com.inventory.inventory_management.dto.SaleRequest;
import com.inventory.inventory_management.dto.SaleResponse;

public interface SalesService {
    SaleResponse createSale(SaleRequest request);

    List<SaleResponse> getAllSales();
}
