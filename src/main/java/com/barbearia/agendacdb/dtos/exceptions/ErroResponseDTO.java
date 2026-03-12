package com.barbearia.agendacdb.dtos.exceptions;

import java.time.LocalDateTime;

public record ErroResponseDTO(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem
) {
}
