package com.barbearia.agendacdb.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.agendacdb.models.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {
    
}
