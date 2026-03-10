package com.barbearia.agendacdb.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.agendacdb.enums.FormaPagamento;
import com.barbearia.agendacdb.enums.StatusAgendamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private ServicoCatalogo servico;

    private String clienteNome;
    private String clienteWhatsapp;
    private String clienteFotoUrl; 

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    private Integer quantidadeBlocos = 1;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    private BigDecimal valorCobrado;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private Boolean isPausa = false; 

    public Agendamento() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Barbeiro getBarbeiro() { return barbeiro; }
    public void setBarbeiro(Barbeiro barbeiro) { this.barbeiro = barbeiro; }

    public ServicoCatalogo getServico() { return servico; }
    public void setServico(ServicoCatalogo servico) { this.servico = servico; }

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

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public Boolean getIsPausa() { return isPausa; }
    public void setIsPausa(Boolean isPausa) { this.isPausa = isPausa; }
}