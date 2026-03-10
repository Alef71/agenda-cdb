package com.barbearia.agendacdb.dtos.admin;

import java.util.UUID;

import com.barbearia.agendacdb.enums.StatusAprovacao;

public class BarbeiroResponseDTO {
    private UUID id;
    private String nome;
    private String cpf;
    private String whatsapp;
    private StatusAprovacao status;
    private String fotoUrl;

    public BarbeiroResponseDTO() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public StatusAprovacao getStatus() { return status; }
    public void setStatus(StatusAprovacao status) { this.status = status; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}
