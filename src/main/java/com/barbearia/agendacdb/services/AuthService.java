package com.barbearia.agendacdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.dtos.auth.LoginRequestDTO;
import com.barbearia.agendacdb.dtos.auth.LoginResponseDTO;
import com.barbearia.agendacdb.dtos.auth.RegistroBarbeiroDTO;
import com.barbearia.agendacdb.enums.RoleBarbeiro;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;

@Service
public class AuthService {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService; 

    public Barbeiro registrar(RegistroBarbeiroDTO dto) {
        if (barbeiroRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado!");
        }

        Barbeiro novoBarbeiro = new Barbeiro();
        novoBarbeiro.setNome(dto.getNome());
        novoBarbeiro.setCpf(dto.getCpf());
        novoBarbeiro.setWhatsapp(dto.getWhatsapp());
        novoBarbeiro.setFotoUrl(dto.getFotoUrl());
        novoBarbeiro.setSenha(passwordEncoder.encode(dto.getSenha()));

        if (barbeiroRepository.count() == 0) {
            novoBarbeiro.setRole(RoleBarbeiro.ADMIN);
            novoBarbeiro.setStatus(StatusAprovacao.APROVADO);
        } else {
            novoBarbeiro.setRole(RoleBarbeiro.BARBEIRO);
            novoBarbeiro.setStatus(StatusAprovacao.PENDENTE);
        }

        return barbeiroRepository.save(novoBarbeiro);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Barbeiro barbeiro = barbeiroRepository.findByCpf(dto.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), barbeiro.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = tokenService.generateToken(barbeiro);
        
        return new LoginResponseDTO(
                token, 
                barbeiro.getId(), 
                barbeiro.getNome(), 
                barbeiro.getRole().name(), 
                barbeiro.getFotoUrl()
        );
    }
}