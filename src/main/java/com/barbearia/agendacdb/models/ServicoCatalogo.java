package com.barbearia.agendacdb.models;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicos_catalogo")
public class ServicoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nomeServico;

    @Column(nullable = false)
    private BigDecimal valorBase;

    public ServicoCatalogo() {}

    public ServicoCatalogo(UUID id, String nomeServico, BigDecimal valorBase) {
        this.id = id;
        this.nomeServico = nomeServico;
        this.valorBase = valorBase;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public BigDecimal getValorBase() { return valorBase; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }
}
