package com.barbearia.agendacdb.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.services.FinanceiroService;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> obterResumoFinanceiro() {
        Barbeiro barbeiroLogado = (Barbeiro) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = barbeiroLogado.getId();

        return ResponseEntity.ok(Map.of(
            "barbeiro", barbeiroLogado.getNome(),
            "faturamentoHoje", financeiroService.calcularFaturamentoDiario(id),
            "faturamentoMesAtual", financeiroService.calcularFaturamentoMensal(id),
            "dataConsulta", java.time.LocalDate.now()
        ));
    }
}