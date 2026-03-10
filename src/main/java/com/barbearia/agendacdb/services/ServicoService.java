package com.barbearia.agendacdb.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.models.Servico;
import com.barbearia.agendacdb.repositories.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public Servico criarServico(Servico servico) {
        return servicoRepository.save(servico);
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico atualizarPreco(UUID id, BigDecimal novoPreco) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        servico.setPrecoBase(novoPreco);
        return servicoRepository.save(servico);
    }

    public void deletarServico(UUID id) {
        servicoRepository.deleteById(id);
    }
}
