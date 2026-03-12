package com.barbearia.agendacdb.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.agendacdb.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    // Lista TODOS os clientes cadastrados que pertencem a um barbeiro específico
    List<Cliente> findAllByBarbeiroId(UUID barbeiroId);

    // Bônus: Busca clientes por parte do nome, mas restrito ao barbeiro (perfeito para um campo de busca)
    List<Cliente> findByNomeContainingIgnoreCaseAndBarbeiroId(String nome, UUID barbeiroId);
}
