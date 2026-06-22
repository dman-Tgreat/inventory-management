package com.inventory.inventory_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {

    @GetMapping("/public/catagories")
    public ResponseEntity<Void> getAllCategory() {
        return ResponseEntity.ok().build();
    }
}
