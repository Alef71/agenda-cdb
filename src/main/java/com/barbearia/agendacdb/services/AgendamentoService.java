package com.barbearia.agendacdb.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.agendacdb.dtos.agenda.AgendamentoRequestDTO;
import com.barbearia.agendacdb.dtos.agenda.AtualizarStatusDTO;
import com.barbearia.agendacdb.dtos.exceptions.AcessoNegadoException;
import com.barbearia.agendacdb.dtos.exceptions.RecursoNaoEncontradoException;
import com.barbearia.agendacdb.dtos.exceptions.RegraNegocioException;
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbeiro não encontrado."));
                
        ServicoCatalogo servico = servicoRepository.findById(dados.getServicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado."));

        Agendamento agendamento = new Agendamento();
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setClienteNome(dados.getClienteNome());
        agendamento.setClienteWhatsapp(dados.getClienteWhatsapp());
        agendamento.setClienteFotoUrl(dados.getClienteFotoUrl());
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
            throw new RegraNegocioException("Já existe um agendamento neste horário para este barbeiro.");
        }
        
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarAgendamentos(UUID barbeiroId, LocalDate dataInicial, LocalDate dataFinal) {
        LocalDateTime inicio = dataInicial.atStartOfDay();
        LocalDateTime fim = dataFinal.atTime(LocalTime.MAX);
        return agendamentoRepository.findByBarbeiroIdAndDataHoraInicioBetween(barbeiroId, inicio, fim);
    }

    @Transactional
    public Agendamento atualizarStatus(UUID id, AtualizarStatusDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));

        validarDonoDoAgendamento(agendamento);

        try {
            StatusAgendamento novoStatus = StatusAgendamento.valueOf(dto.status().toUpperCase());
            agendamento.setStatus(novoStatus);

            if (novoStatus == StatusAgendamento.CONCLUIDO) {
                if (dto.valorCobrado() != null) agendamento.setValorCobrado(dto.valorCobrado());
                
                if (dto.formaPagamento() != null) {
                    agendamento.setFormaPagamento(FormaPagamento.valueOf(dto.formaPagamento().toUpperCase()));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Status ou Forma de Pagamento inválidos.");
        }

        return agendamentoRepository.save(agendamento);
    }

    public BigDecimal calcularFaturamento(UUID barbeiroId, LocalDate dataInicial, LocalDate dataFinal) {
        LocalDateTime inicio = dataInicial.atStartOfDay();
        LocalDateTime fim = dataFinal.atTime(LocalTime.MAX);

        BigDecimal faturamento = agendamentoRepository.somarFaturamentoPeriodo(barbeiroId, inicio, fim);
        
        return (faturamento == null) ? BigDecimal.ZERO : faturamento;
    }

    @Transactional
    public void excluirAgendamento(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado!"));

        validarDonoDoAgendamento(agendamento);

        agendamentoRepository.delete(agendamento);
    }

    private void validarDonoDoAgendamento(Agendamento agendamento) {
        Barbeiro barbeiroLogado = (Barbeiro) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!agendamento.getBarbeiro().getId().equals(barbeiroLogado.getId())) {
            throw new AcessoNegadoException("Você não tem permissão para alterar um agendamento de outro barbeiro.");
        }
    }
}