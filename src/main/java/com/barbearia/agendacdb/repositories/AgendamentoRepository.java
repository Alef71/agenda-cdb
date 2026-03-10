package com.barbearia.agendacdb.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.agendacdb.models.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    List<Agendamento> findByBarbeiroIdAndDataHoraInicioBetween(UUID barbeiroId, LocalDateTime start, LocalDateTime end);
}
