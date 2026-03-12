package com.barbearia.agendacdb.dtos.agenda;

import java.math.BigDecimal;

public record FinalizarAgendamentoDTO(
        BigDecimal valorCobrado,
        String formaPagamento
) {
}
