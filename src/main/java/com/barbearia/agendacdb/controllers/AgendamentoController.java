package com.barbearia.agendacdb.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.barbearia.agendacdb.dtos.agenda.AgendamentoRequestDTO;
import com.barbearia.agendacdb.dtos.agenda.FinalizarAgendamentoDTO;
import com.barbearia.agendacdb.models.Agendamento;
import com.barbearia.agendacdb.services.AgendamentoService;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> criar(@RequestBody AgendamentoRequestDTO dados) {
        return ResponseEntity.ok(agendamentoService.criarAgendamento(dados));
    }

    @GetMapping("/barbeiro/{barbeiroId}/dia/{data}")
    public ResponseEntity<List<Agendamento>> listarPorDia(
            @PathVariable UUID barbeiroId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        return ResponseEntity.ok(agendamentoService.listarAgendaDoDia(barbeiroId, data));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Object> finalizarAgendamento(
            @PathVariable UUID id,
            @RequestBody FinalizarAgendamentoDTO dto) {
        
        Agendamento agendamentoFinalizado = agendamentoService.finalizarAgendamento(id, dto);
        return ResponseEntity.ok(agendamentoFinalizado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Object> cancelarAgendamento(@PathVariable UUID id) {
        Agendamento agendamentoCancelado = agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.ok(agendamentoCancelado);
    }

    @GetMapping("/agenda-do-dia")
    public ResponseEntity<List<Agendamento>> listarAgendaDoDia(
            @RequestParam UUID barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<Agendamento> agenda = agendamentoService.listarAgendaDoDia(barbeiroId, data);
        return ResponseEntity.ok(agenda);
    }

    @PatchMapping("/{id}/furo")
    public ResponseEntity<Object> marcarComoFuro(@PathVariable UUID id) {
        Agendamento agendamentoFuro = agendamentoService.marcarComoFuro(id);
        return ResponseEntity.ok(agendamentoFuro);
    }

    @GetMapping("/faturamento-do-dia")
    public ResponseEntity<Object> consultarFaturamentoDoDia(
            @RequestParam UUID barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        BigDecimal faturamento = agendamentoService.calcularFaturamentoDoDia(barbeiroId, data);
    
        return ResponseEntity.ok(java.util.Map.of("faturamentoTotal", faturamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAgendamento(@PathVariable UUID id) {
        // Chama o service para deletar no banco de dados
        agendamentoService.excluirAgendamento(id); 
        
        // Retorna 204 (No Content), que é o padrão correto para DELETE
        return ResponseEntity.noContent().build(); 
    }
}