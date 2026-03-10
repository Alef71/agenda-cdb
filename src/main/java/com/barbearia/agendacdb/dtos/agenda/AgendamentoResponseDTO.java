package com.barbearia.agendacdb.dtos.agenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.agendacdb.enums.StatusAgendamento;

public class AgendamentoResponseDTO {
    private UUID id;
    private String servicoNome;
    private String clienteNome;
    private String clienteWhatsapp;
    private String clienteFotoUrl;
    private LocalDateTime dataHoraInicio;
    private Integer quantidadeBlocos;
    private StatusAgendamento status;
    private BigDecimal valorCobrado;
    private Boolean isPausa;

    public AgendamentoResponseDTO() {}

    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getServicoNome() { return servicoNome; }
    public void setServicoNome(String servicoNome) { this.servicoNome = servicoNome; }
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
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
    public Boolean getIsPausa() { return isPausa; }
    public void setIsPausa(Boolean isPausa) { this.isPausa = isPausa; }
}