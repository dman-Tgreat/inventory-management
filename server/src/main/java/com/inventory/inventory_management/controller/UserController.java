package com.inventory.inventory_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management.dto.UserResponse;
import com.inventory.inventory_management.service.Interface.UserService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccessControlService accessControlService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-Role") String role, @PathVariable Long id) {
        accessControlService.requireAdmin(role);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
