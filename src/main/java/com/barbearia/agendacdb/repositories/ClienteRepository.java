package com.barbearia.agendacdb.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.agendacdb.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    List<Cliente> findAllByBarbeiroId(UUID barbeiroId);
    List<Cliente> findByNomeContainingIgnoreCaseAndBarbeiroId(String nome, UUID barbeiroId);
}
