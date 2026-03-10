package com.barbearia.agendacdb.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, UUID> {
    Optional<Barbeiro> findByCpf(String cpf);
    List<Barbeiro> findByStatus(StatusAprovacao status);
}
