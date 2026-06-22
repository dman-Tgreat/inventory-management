package com.inventory.inventory_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management.dto.DashboardResponse;
import com.inventory.inventory_management.service.Interface.DashboardService;
import com.inventory.inventory_management.service.impl.AccessControlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final AccessControlService accessControlService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@RequestHeader("X-Role") String role) {
        accessControlService.requireAdmin(role);
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}
