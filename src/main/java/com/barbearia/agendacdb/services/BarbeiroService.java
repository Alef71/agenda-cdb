package com.barbearia.agendacdb.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.dtos.AtualizarPerfilDTO;
import com.barbearia.agendacdb.dtos.AtualizarSenhaDTO;
import com.barbearia.agendacdb.dtos.exceptions.RecursoNaoEncontradoException;
import com.barbearia.agendacdb.dtos.exceptions.RegraNegocioException;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;

@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Barbeiro> listarTodos() {
        return repository.findAll();
    }

    public void excluir(UUID id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Barbeiro não encontrado.");
        }
        repository.deleteById(id);
    }

    public Barbeiro aprovar(UUID id) {
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbeiro não encontrado."));
        
        barbeiro.setStatus(StatusAprovacao.APROVADO);
        return repository.save(barbeiro);
    }

    public Barbeiro atualizarPerfil(UUID id, AtualizarPerfilDTO dto) {
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbeiro não encontrado."));
        
        barbeiro.setNome(dto.nome);
        barbeiro.setWhatsapp(dto.whatsapp);
        barbeiro.setFotoUrl(dto.fotoUrl);

        return repository.save(barbeiro);
    }

    public void redefinirSenha(UUID id, AtualizarSenhaDTO dto) {
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbeiro não encontrado."));
        
        if (!passwordEncoder.matches(dto.senhaAntiga, barbeiro.getSenha())) {
            throw new RegraNegocioException("Senha antiga incorreta!");
        }

        barbeiro.setSenha(passwordEncoder.encode(dto.novaSenha));
        repository.save(barbeiro);
    }
}