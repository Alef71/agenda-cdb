package com.barbearia.agendacdb.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.dtos.admin.BarbeiroResponseDTO;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;

@Service
public class AdminService {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    public List<BarbeiroResponseDTO> listarPendentes() {
        return barbeiroRepository.findAll().stream()
                .filter(barbeiro -> barbeiro.getStatus() == StatusAprovacao.PENDENTE)
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void aprovarBarbeiro(UUID id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
        
        barbeiro.setStatus(StatusAprovacao.APROVADO);
        barbeiroRepository.save(barbeiro);
    }

    public void recusarBarbeiro(UUID id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
        
        barbeiro.setStatus(StatusAprovacao.RECUSADO);
        barbeiroRepository.save(barbeiro);
    }

    private BarbeiroResponseDTO converterParaDTO(Barbeiro barbeiro) {
        BarbeiroResponseDTO dto = new BarbeiroResponseDTO();
        dto.setId(barbeiro.getId());
        dto.setNome(barbeiro.getNome());
        dto.setCpf(barbeiro.getCpf());
        dto.setWhatsapp(barbeiro.getWhatsapp());
        dto.setStatus(barbeiro.getStatus());
        dto.setFotoUrl(barbeiro.getFotoUrl());
        return dto;
    }
}
