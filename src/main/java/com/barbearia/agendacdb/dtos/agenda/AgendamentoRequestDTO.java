package com.barbearia.agendacdb.dtos.agenda;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendamentoRequestDTO {
    private UUID barbeiroId;
    private UUID servicoId;
    private String clienteNome;
    private String clienteWhatsapp;
    private String clienteFotoUrl; 
    private LocalDateTime dataHoraInicio;
    private Integer quantidadeBlocos;
    private Boolean isPausa;
    private String observacao;

    public AgendamentoRequestDTO() {}

    public UUID getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(UUID barbeiroId) { this.barbeiroId = barbeiroId; }

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

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}