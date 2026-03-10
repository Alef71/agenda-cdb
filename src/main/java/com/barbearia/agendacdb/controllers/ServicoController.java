package com.barbearia.agendacdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.models.Servico;
import com.barbearia.agendacdb.services.ServicoService;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Servico> criar(@RequestBody Servico servico) {
        return ResponseEntity.ok(servicoService.criarServico(servico));
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    @PutMapping("/{id}/preco")
    public ResponseEntity<Servico> atualizarPreco(@PathVariable UUID id, @RequestBody Servico dados) {
        return ResponseEntity.ok(servicoService.atualizarPreco(id, dados.getPrecoBase()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable UUID id) {
        servicoService.deletarServico(id);
        return ResponseEntity.ok("Serviço removido com sucesso!");
    }
}
