package com.example.payments_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {
@PostMapping("/login")

public ResponseEntity<String> login(@RequestBody LoginRequest request) {
String email = request.email();
String senha = request.senha();

if (email == null || email.isBlank()) {
return ResponseEntity.badRequest().body("Email obrigatório");
}
if (senha == null || senha.isBlank()) {

return ResponseEntity.badRequest().body("Senha obrigatória");
}
return ResponseEntity.ok("Dados recebidos!");
}
} 

