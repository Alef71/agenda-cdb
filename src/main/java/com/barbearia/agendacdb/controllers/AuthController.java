package com.barbearia.agendacdb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.dtos.auth.LoginRequestDTO;
import com.barbearia.agendacdb.dtos.auth.LoginResponseDTO;
import com.barbearia.agendacdb.dtos.auth.RegistroBarbeiroDTO;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth") 
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registrar")
    public ResponseEntity<Barbeiro> registrar(@RequestBody @Valid RegistroBarbeiroDTO data) {
        Barbeiro novoBarbeiro = authService.registrar(data);
        return ResponseEntity.ok(novoBarbeiro);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        LoginResponseDTO response = authService.login(data);
        return ResponseEntity.ok(response);
    }
}
