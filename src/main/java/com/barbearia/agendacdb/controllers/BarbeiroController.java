package com.barbearia.agendacdb.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Importação adicionada
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
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;

@RestController
@RequestMapping("/api/barbeiros")
public class BarbeiroController {

    @Autowired
    private BarbeiroRepository repository;

    // --- AQUI ESTÁ A INJEÇÃO DO PASSWORD ENCODER ---
    @Autowired
    private PasswordEncoder passwordEncoder; 

    // Retorna todos os barbeiros para o Admin ver
    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarTodos() {
        List<Barbeiro> barbeiros = repository.findAll();
        return ResponseEntity.ok(barbeiros);
    }

    // Rota para deletar/recusar um barbeiro usando UUID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) { 
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Rota para APROVAR o barbeiro (muda o status dele)
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Barbeiro> aprovar(@PathVariable UUID id) {
        Optional<Barbeiro> barbeiroOptional = repository.findById(id);
        
        if (barbeiroOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Barbeiro barbeiro = barbeiroOptional.get();
        barbeiro.setStatus(StatusAprovacao.APROVADO);
        
        repository.save(barbeiro);
        return ResponseEntity.ok(barbeiro);
    }

    // ==========================================
    // NOVAS ROTAS (FASE 2)
    // ==========================================

    // Rota para o barbeiro editar NOME, WHATSAPP e FOTO
    @PutMapping("/{id}/perfil")
    public ResponseEntity<Barbeiro> atualizarPerfil(
            @PathVariable UUID id, 
            @RequestBody AtualizarPerfilDTO dto) {
        
        Optional<Barbeiro> barbeiroOptional = repository.findById(id);
        if (barbeiroOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Barbeiro barbeiro = barbeiroOptional.get();
        barbeiro.setNome(dto.nome);
        barbeiro.setWhatsapp(dto.whatsapp);
        barbeiro.setFotoUrl(dto.fotoUrl);

        return ResponseEntity.ok(repository.save(barbeiro));
    }

    // Rota para o barbeiro redefinir a SENHA
    @PatchMapping("/{id}/senha")
    public ResponseEntity<String> redefinirSenha(
            @PathVariable UUID id, 
            @RequestBody AtualizarSenhaDTO dto) {
        
        Optional<Barbeiro> barbeiroOptional = repository.findById(id);
        if (barbeiroOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Barbeiro barbeiro = barbeiroOptional.get();
        
        // CORREÇÃO: Usando o passwordEncoder para comparar a senha digitada com a do banco
        if (!passwordEncoder.matches(dto.senhaAntiga, barbeiro.getSenha())) {
            return ResponseEntity.badRequest().body("Senha antiga incorreta!");
        }

        // CORREÇÃO: Criptografando a NOVA senha antes de salvar no banco
        barbeiro.setSenha(passwordEncoder.encode(dto.novaSenha)); 
        repository.save(barbeiro);

        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }
}