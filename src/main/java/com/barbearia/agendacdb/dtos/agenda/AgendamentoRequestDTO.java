package com.barbearia.agendacdb.dtos.agenda;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendamentoRequestDTO {
    private UUID servicoId;
    private String clienteNome;
    private String clienteWhatsapp;
    private String clienteFotoUrl;
    private LocalDateTime dataHoraInicio;
    private Integer quantidadeBlocos;
    private Boolean isPausa;

    public AgendamentoRequestDTO() {}

    public UUID getServicoId() { return servicoId; }
    public void setServicoId(UUID servicoId) { this.servicoId = servicoId; }
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public String getClienteWhatsapp() { return clienteWhatsapp; }
    public void setClienteWhatsapp(String clienteWhatsapp) { this.clienteWhatsapp = clienteWhatsapp; }
    public String getClienteFotoUrl() { return clienteFotoUrl; }
    public void setClienteFotoUrl(String clienteFotoUrl) { this.clienteFotoUrl = clienteFotoUrl; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public Integer getQuantidadeBlocos() { return quantidadeBlocos; }
    public void setQuantidadeBlocos(Integer quantidadeBlocos) { this.quantidadeBlocos = quantidadeBlocos; }
    public Boolean getIsPausa() { return isPausa; }
    public void setIsPausa(Boolean isPausa) { this.isPausa = isPausa; }
}
