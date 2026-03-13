package com.barbearia.agendacdb.dtos.exceptions;

import java.time.LocalDateTime;

public record ErroResponseDTO(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem
) {}