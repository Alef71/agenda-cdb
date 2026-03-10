package com.barbearia.agendacdb.dtos.auth;

import java.util.UUID;

public class LoginResponseDTO {
    private String token;
    private UUID barbeiroId;
    private String nome;
    private String role;
    private String fotoUrl;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, UUID barbeiroId, String nome, String role, String fotoUrl) {
        this.token = token;
        this.barbeiroId = barbeiroId;
        this.nome = nome;
        this.role = role;
        this.fotoUrl = fotoUrl;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UUID getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(UUID barbeiroId) { this.barbeiroId = barbeiroId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}
