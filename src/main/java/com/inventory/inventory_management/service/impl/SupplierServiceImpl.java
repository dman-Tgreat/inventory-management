package com.inventory.inventory_management.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.SupplierRequest;
import com.inventory.inventory_management.dto.SupplierResponse;
import com.inventory.inventory_management.entity.Supplier;
import com.inventory.inventory_management.repository.SupplierRepository;
import com.inventory.inventory_management.service.SupplierService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        return toResponse(supplierRepository.save(supplier));
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::toResponse).toList();
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress());
    }
}
