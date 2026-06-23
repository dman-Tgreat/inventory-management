package com.inventory.inventory_management.security;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String generateToken(String subject) {
        return subject;
    }

    public String extractSubject(String token) {
        return token;
    }
}
