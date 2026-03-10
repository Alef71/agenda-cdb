package com.barbearia.agendacdb.models;

import com.barbearia.agendacdb.enums.RoleBarbeiro;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import jakarta.persistence.*;
import java.util.UUID;

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

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private RoleBarbeiro role;

    @Enumerated(EnumType.STRING)
    private StatusAprovacao status;

    private String fotoUrl;

    
    public Barbeiro() {}

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
}
