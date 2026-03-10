package com.barbearia.agendacdb.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.agendacdb.models.VendaLoja;

public interface VendaLojaRepository extends JpaRepository<VendaLoja, UUID> {
    List<VendaLoja> findByBarbeiroIdAndDataVendaBetween(UUID barbeiroId, LocalDateTime start, LocalDateTime end);
}
