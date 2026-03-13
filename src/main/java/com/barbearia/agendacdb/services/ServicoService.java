package com.barbearia.agendacdb.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.dtos.exceptions.RecursoNaoEncontradoException;
import com.barbearia.agendacdb.models.ServicoCatalogo;
import com.barbearia.agendacdb.repositories.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public ServicoCatalogo atualizar(UUID id, ServicoCatalogo dadosAtualizados) {
        ServicoCatalogo servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado com o ID: " + id));

        servico.setNomeServico(dadosAtualizados.getNomeServico());
        servico.setValorBase(dadosAtualizados.getValorBase());

        return servicoRepository.save(servico);
    }

    public ServicoCatalogo atualizarPreco(UUID id, BigDecimal novoValor) {
        ServicoCatalogo servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado com o ID: " + id));

        servico.setValorBase(novoValor);
        return servicoRepository.save(servico);
    }

    public ServicoCatalogo salvar(ServicoCatalogo servico) {
        return servicoRepository.save(servico);
    }

    public List<ServicoCatalogo> listarTodos() {
        return servicoRepository.findAll();
    }

    public void deletar(UUID id) {
        if (!servicoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não foi possível deletar. Serviço não encontrado.");
        }
        servicoRepository.deleteById(id);
    }
}