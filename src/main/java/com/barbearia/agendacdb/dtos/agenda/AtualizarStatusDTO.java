package com.barbearia.agendacdb.dtos.agenda;

import java.math.BigDecimal;

import com.barbearia.agendacdb.enums.FormaPagamento;
import com.barbearia.agendacdb.enums.StatusAgendamento;

public class AtualizarStatusDTO {
    private StatusAgendamento status;
    private BigDecimal valorCobrado;
    private FormaPagamento formaPagamento;

    public AtualizarStatusDTO() {}

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
}
