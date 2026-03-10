package com.barbearia.agendacdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.dtos.admin.BarbeiroResponseDTO;
import com.barbearia.agendacdb.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/barbeiros/pendentes")
    public ResponseEntity<List<BarbeiroResponseDTO>> listarPendentes() {
        return ResponseEntity.ok(adminService.listarPendentes());
    }

    @PutMapping("/barbeiros/{id}/aprovar")
    public ResponseEntity<String> aprovarBarbeiro(@PathVariable UUID id) {
        adminService.aprovarBarbeiro(id);
        return ResponseEntity.ok("Barbeiro aprovado com sucesso!");
    }

    @PutMapping("/barbeiros/{id}/recusar")
    public ResponseEntity<String> recusarBarbeiro(@PathVariable UUID id) {
        adminService.recusarBarbeiro(id);
        return ResponseEntity.ok("Barbeiro recusado com sucesso.");
    }
}
