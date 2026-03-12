package com.barbearia.agendacdb.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.barbearia.agendacdb.enums.RoleBarbeiro;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String whatsapp;
    
    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private RoleBarbeiro role;

    @Enumerated(EnumType.STRING)
    private StatusAprovacao status;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    // === NOVA PARTE: Mapeamento dos Agendamentos em Cascata ===
    // O @JsonIgnore evita problemas de loop infinito ao retornar a resposta na API
    @JsonIgnore 
    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Agendamento> agendamentos = new ArrayList<>();
    // ==========================================================

    public Barbeiro() {}

    // Getters e Setters originais
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public RoleBarbeiro getRole() { return role; }
    public void setRole(RoleBarbeiro role) { this.role = role; }

    public StatusAprovacao getStatus() { return status; }
    public void setStatus(StatusAprovacao status) { this.status = status; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public List<Agendamento> getAgendamentos() { return agendamentos; }
    public void setAgendamentos(List<Agendamento> agendamentos) { this.agendamentos = agendamentos; }
}