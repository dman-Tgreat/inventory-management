package com.inventory.inventory_management.service;

import java.util.List;

import com.inventory.inventory_management.dto.UserResponse;

public interface UserService {
    List<UserResponse> getAllUsers();

    void deleteUser(Long id);
}
