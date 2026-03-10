package com.barbearia.agendacdb.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.agendacdb.models.ServicoCatalogo;

public interface ServicoCatalogoRepository extends JpaRepository<ServicoCatalogo, UUID> {
}
