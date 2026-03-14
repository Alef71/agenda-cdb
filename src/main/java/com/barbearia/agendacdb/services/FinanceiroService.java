package com.barbearia.agendacdb.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.repositories.AgendamentoRepository;

@Service
public class FinanceiroService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

   
    public BigDecimal calcularFaturamentoDiario(UUID barbeiroId) {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim = LocalDate.now().atTime(LocalTime.MAX);

        BigDecimal faturamento = agendamentoRepository.somarFaturamentoPeriodo(barbeiroId, inicio, fim);
        
        return (faturamento == null) ? BigDecimal.ZERO : faturamento;
    }

    public BigDecimal calcularFaturamentoMensal(UUID barbeiroId) {
        LocalDateTime inicioMes = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        
        LocalDateTime fimMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        BigDecimal faturamento = agendamentoRepository.somarFaturamentoPeriodo(barbeiroId, inicioMes, fimMes);
        
        return (faturamento == null) ? BigDecimal.ZERO : faturamento;
    }
}