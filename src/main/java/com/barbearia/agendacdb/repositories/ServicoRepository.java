package com.barbearia.agendacdb.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.agendacdb.models.ServicoCatalogo; 

@Repository
public interface ServicoRepository extends JpaRepository<ServicoCatalogo, UUID> {
}