package com.meuportfolio.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        // Este endpoint só é alcançado se o SecurityFilter autenticar o usuário
        return ResponseEntity.ok("Acesso Autorizado! Token Válido.");
    }
}