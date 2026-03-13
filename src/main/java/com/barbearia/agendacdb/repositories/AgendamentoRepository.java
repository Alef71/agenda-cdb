package com.barbearia.agendacdb.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.barbearia.agendacdb.models.Agendamento;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    List<Agendamento> findByBarbeiroIdAndDataHoraInicioBetween(UUID barbeiroId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
           "AND a.status != 'CANCELADO' AND a.status != 'FURO' " +
           "AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)")
    boolean existsConflitoHorario(@Param("barbeiroId") UUID barbeiroId, 
                                  @Param("inicio") LocalDateTime inicio, 
                                  @Param("fim") LocalDateTime fim);

    @Query("SELECT SUM(a.valorCobrado) FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
           "AND a.status = 'CONCLUIDO' " +
           "AND (a.dataHoraInicio >= :inicio AND a.dataHoraInicio <= :fim)")
    BigDecimal somarFaturamentoPeriodo(@Param("barbeiroId") UUID barbeiroId, 
                                       @Param("inicio") LocalDateTime inicio, 
                                       @Param("fim") LocalDateTime fim);
}