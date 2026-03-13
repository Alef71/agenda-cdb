package com.barbearia.agendacdb.dtos.agenda;

import java.math.BigDecimal;

public record AtualizarStatusDTO(
        String status, 
        BigDecimal valorCobrado, 
        String formaPagamento    
) {}