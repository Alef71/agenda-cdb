package com.barbearia.agendacdb.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.dtos.agenda.AgendamentoRequestDTO;
import com.barbearia.agendacdb.dtos.agenda.AtualizarStatusDTO;
import com.barbearia.agendacdb.dtos.exceptions.AcessoNegadoException;
import com.barbearia.agendacdb.models.Agendamento;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.services.AgendamentoService;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> criar(@RequestBody AgendamentoRequestDTO dados) {
        validarPermissao(dados.getBarbeiroId(), "criar agendamento");
        return ResponseEntity.ok(agendamentoService.criarAgendamento(dados));
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos(
            @RequestParam UUID barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        validarPermissao(barbeiroId, "listar agendamentos");
        
        LocalDate fim = (dataFinal != null) ? dataFinal : dataInicial; 
        return ResponseEntity.ok(agendamentoService.listarAgendamentos(barbeiroId, dataInicial, fim));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody AtualizarStatusDTO dto) {
        
        return ResponseEntity.ok(agendamentoService.atualizarStatus(id, dto));
    }

    @GetMapping("/faturamento")
    public ResponseEntity<Object> consultarFaturamento(
            @RequestParam UUID barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        validarPermissao(barbeiroId, "consultar faturamento");

        LocalDate fim = (dataFinal != null) ? dataFinal : dataInicial;
        BigDecimal faturamento = agendamentoService.calcularFaturamento(barbeiroId, dataInicial, fim);
        
        return ResponseEntity.ok(java.util.Map.of("faturamentoTotal", faturamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAgendamento(@PathVariable UUID id) {
        agendamentoService.excluirAgendamento(id); 
        return ResponseEntity.noContent().build(); 
    }

    private void validarPermissao(UUID barbeiroIdAlvo, String acao) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Barbeiro barbeiroLogado = (Barbeiro) authentication.getPrincipal();

        if (!barbeiroLogado.getId().equals(barbeiroIdAlvo)) {
            throw new AcessoNegadoException("Acesso negado: Você não tem permissão para " + acao + " de outro barbeiro.");
        }
    }
}