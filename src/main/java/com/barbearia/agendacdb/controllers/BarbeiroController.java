package com.barbearia.agendacdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.dtos.AtualizarPerfilDTO;
import com.barbearia.agendacdb.dtos.AtualizarSenhaDTO;
import com.barbearia.agendacdb.dtos.exceptions.AcessoNegadoException;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.services.BarbeiroService;

@RestController
@RequestMapping("/api/barbeiros")
public class BarbeiroController {

    @Autowired
    private BarbeiroService barbeiroService;

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarTodos() {
        return ResponseEntity.ok(barbeiroService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        barbeiroService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Barbeiro> aprovar(@PathVariable UUID id) {
        return ResponseEntity.ok(barbeiroService.aprovar(id));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<Object> atualizarPerfil(
            @PathVariable UUID id, 
            @RequestBody AtualizarPerfilDTO dto) {
        
        validarPermissao(id, "alterar o perfil");
        return ResponseEntity.ok(barbeiroService.atualizarPerfil(id, dto));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<String> redefinirSenha(
            @PathVariable UUID id, 
            @RequestBody AtualizarSenhaDTO dto) {
        
        validarPermissao(id, "alterar a senha");
        barbeiroService.redefinirSenha(id, dto);
        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }

    private void validarPermissao(UUID idDaUrl, String acao) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Barbeiro barbeiroLogado = (Barbeiro) authentication.getPrincipal();

        if (!barbeiroLogado.getId().equals(idDaUrl)) {
            throw new AcessoNegadoException("Acesso negado: Você não tem permissão para " + acao + " de outro barbeiro.");
        }
    }
}