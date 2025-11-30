package com.meuportfolio.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminDashboard(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok("Bem-vindo ao Dashboard de ADMIN, " + username + "!");
    }
}