package com.barbearia.agendacdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.models.ServicoCatalogo;
import com.barbearia.agendacdb.services.ServicoService;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<ServicoCatalogo> criar(@RequestBody ServicoCatalogo servico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.salvar(servico));
    }

    @GetMapping
    public ResponseEntity<List<ServicoCatalogo>> listar() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    @PutMapping("/{id}/preco")
    public ResponseEntity<ServicoCatalogo> atualizarPreco(
            @PathVariable UUID id, 
            @RequestBody ServicoCatalogo dados) {
        return ResponseEntity.ok(servicoService.atualizarPreco(id, dados.getValorBase()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoCatalogo> atualizarServico(
            @PathVariable UUID id, 
            @RequestBody ServicoCatalogo dadosAtualizados) {
        return ResponseEntity.ok(servicoService.atualizar(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}