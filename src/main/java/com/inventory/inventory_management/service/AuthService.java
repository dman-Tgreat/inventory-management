package com.inventory.inventory_management.service;

import com.inventory.inventory_management.dto.AuthResponse;
import com.inventory.inventory_management.dto.LoginRequest;
import com.inventory.inventory_management.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
