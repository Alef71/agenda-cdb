package com.barbearia.agendacdb.GlobalExceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.barbearia.agendacdb.dtos.exceptions.AcessoNegadoException;
import com.barbearia.agendacdb.dtos.exceptions.ErroResponseDTO;
import com.barbearia.agendacdb.dtos.exceptions.RecursoNaoEncontradoException;
import com.barbearia.agendacdb.dtos.exceptions.RegraNegocioException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Tratamento para RECURSO NÃO ENCONTRADO (Status 404)
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleNotFound(RecursoNaoEncontradoException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 2. Tratamento para ACESSO NEGADO / PROTEÇÃO IDOR (Status 403)
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponseDTO> handleForbidden(AcessoNegadoException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // 3. Tratamento para REGRAS DE NEGÓCIO E VALIDAÇÕES (Status 400)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleBadRequest(RegraNegocioException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de Negócio",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGeneralError(Exception ex) {
        ex.printStackTrace(); 

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno no Servidor",
                "Ocorreu um erro inesperado no sistema. Por favor, tente novamente mais tarde."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}