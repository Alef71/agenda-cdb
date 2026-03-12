package com.barbearia.agendacdb.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.agendacdb.dtos.agenda.AgendamentoRequestDTO;
import com.barbearia.agendacdb.dtos.agenda.FinalizarAgendamentoDTO;
import com.barbearia.agendacdb.enums.FormaPagamento;
import com.barbearia.agendacdb.enums.StatusAgendamento;
import com.barbearia.agendacdb.models.Agendamento;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.models.ServicoCatalogo;
import com.barbearia.agendacdb.repositories.AgendamentoRepository;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;
import com.barbearia.agendacdb.repositories.ServicoRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public Agendamento criarAgendamento(AgendamentoRequestDTO dados) {
        
        Barbeiro barbeiro = barbeiroRepository.findById(dados.getBarbeiroId())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));
                
        ServicoCatalogo servico = servicoRepository.findById(dados.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        
        Agendamento agendamento = new Agendamento();
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setClienteNome(dados.getClienteNome());
        agendamento.setClienteWhatsapp(dados.getClienteWhatsapp());
        agendamento.setDataHoraInicio(dados.getDataHoraInicio());
        agendamento.setQuantidadeBlocos(dados.getQuantidadeBlocos());
        agendamento.setIsPausa(dados.getIsPausa());
        agendamento.setObservacao(dados.getObservacao());
        
        int minutosPorBloco = 30; 
        int duracaoTotalMinutos = agendamento.getQuantidadeBlocos() * minutosPorBloco;

        LocalDateTime dataHoraFim = agendamento.getDataHoraInicio().plusMinutes(duracaoTotalMinutos);
        agendamento.setDataHoraFim(dataHoraFim);

        boolean temConflito = agendamentoRepository.existsConflitoHorario(
                agendamento.getBarbeiro().getId(), 
                agendamento.getDataHoraInicio(), 
                dataHoraFim
        );

        if (temConflito) {
            throw new RuntimeException("Já existe um agendamento neste horário para este barbeiro.");
        }
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarAgendaDoDia(UUID barbeiroId, LocalDate data) {
        
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByBarbeiroIdAndDataHoraInicioBetween(barbeiroId, inicioDoDia, fimDoDia);
    }

    @Transactional
    public Agendamento finalizarAgendamento(UUID id, FinalizarAgendamentoDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        agendamento.setValorCobrado(dto.valorCobrado());
        agendamento.setFormaPagamento(FormaPagamento.valueOf(dto.formaPagamento().toUpperCase()));
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento cancelarAgendamento(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento marcarComoFuro(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        agendamento.setStatus(StatusAgendamento.FURO);

        return agendamentoRepository.save(agendamento);
    }

    public BigDecimal calcularFaturamentoDoDia(UUID barbeiroId, LocalDate data) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);

        BigDecimal faturamento = agendamentoRepository.somarFaturamentoDoDia(barbeiroId, inicioDoDia, fimDoDia);
        
        if (faturamento == null) {
            return BigDecimal.ZERO;
        }
        
        return faturamento;
    }

    // --- NOVO MÉTODO ADICIONADO AQUI ---
    @Transactional
    public void excluirAgendamento(UUID id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado!");
        }
        agendamentoRepository.deleteById(id);
    }
}