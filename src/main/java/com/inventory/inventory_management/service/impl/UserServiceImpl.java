package com.inventory.inventory_management.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.UserResponse;
import com.inventory.inventory_management.entity.User;
import com.inventory.inventory_management.exception.ResourceNotFoundException;
import com.inventory.inventory_management.repository.UserRepository;
import com.inventory.inventory_management.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        String role = user.getRole() == null ? null : user.getRole().getRoleName();
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), role);
    }
}
