package com.barbearia.agendacdb.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.agendacdb.models.Cliente;
import com.barbearia.agendacdb.repositories.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizar(UUID id, Cliente dadosAtualizados) {
        Cliente clienteExistente = buscarPorId(id);
        
        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setTelefone(dadosAtualizados.getTelefone());
        
        if (dadosAtualizados.getFotoPerfil() != null) {
            clienteExistente.setFotoPerfil(dadosAtualizados.getFotoPerfil());
        }

        return clienteRepository.save(clienteExistente);
    }

    public void deletar(UUID id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
