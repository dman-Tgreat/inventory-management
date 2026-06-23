package com.inventory.inventory_management.service.Interface;

import java.util.List;

import com.inventory.inventory_management.dto.SupplierRequest;
import com.inventory.inventory_management.dto.SupplierResponse;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);

    List<SupplierResponse> getAllSuppliers();
}
