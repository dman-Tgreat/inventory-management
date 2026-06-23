package com.inventory.inventory_management.service.impl;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.exception.ForbiddenActionException;

@Service
public class AccessControlService {
    public void requireAdmin(String role) {
        if (!isAdmin(role)) {
            throw new ForbiddenActionException("Only admin users can perform this action");
        }
    }

    public void requireAdminOrEmployee(String role) {
        if (!isAdmin(role) && !isEmployee(role)) {
            throw new ForbiddenActionException("Admin or employee role is required");
        }
    }

    private boolean isAdmin(String role) {
        return "ADMIN".equalsIgnoreCase(normalize(role));
    }

    private boolean isEmployee(String role) {
        return "EMPLOYEE".equalsIgnoreCase(normalize(role));
    }

    private String normalize(String role) {
        return role == null ? "" : role.trim().replace("ROLE_", "");
    }
}
