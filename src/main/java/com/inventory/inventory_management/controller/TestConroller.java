package com.inventory.inventory_management.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestConroller {

    @GetMapping("/test")
    public String Display(){
        return "API is working fine!";
    }
    
}
