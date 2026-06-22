package com.inventory.inventory_management.service.impl;

import org.springframework.stereotype.Service;

import com.inventory.inventory_management.dto.AuthResponse;
import com.inventory.inventory_management.dto.LoginRequest;
import com.inventory.inventory_management.dto.RegisterRequest;
import com.inventory.inventory_management.entity.Role;
import com.inventory.inventory_management.entity.User;
import com.inventory.inventory_management.exception.BadRequestException;
import com.inventory.inventory_management.exception.ResourceNotFoundException;
import com.inventory.inventory_management.repository.RoleRepository;
import com.inventory.inventory_management.repository.UserRepository;
import com.inventory.inventory_management.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        String roleName = normalizeRole(role.getRoleName());
        if (!"ADMIN".equals(roleName) && !"EMPLOYEE".equals(roleName)) {
            throw new BadRequestException("Role must be Admin or Employee");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(role);

        return toResponse(userRepository.save(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() == null ? null : normalizeRole(user.getRole().getRoleName()));
    }

    private String normalizeRole(String roleName) {
        return roleName == null ? "" : roleName.trim().replace("ROLE_", "").toUpperCase();
    }
}
