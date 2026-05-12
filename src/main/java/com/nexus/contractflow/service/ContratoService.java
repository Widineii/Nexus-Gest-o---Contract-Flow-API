package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.request.ContratoRequestDTO;
import com.nexus.contractflow.dto.request.SuspenderContratoDTO;
import com.nexus.contractflow.dto.response.ContratoResponseDTO;
import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.entity.Fornecedor;
import com.nexus.contractflow.entity.enums.StatusContrato;
import com.nexus.contractflow.exception.RecursoDuplicadoException;
import com.nexus.contractflow.exception.RecursoNaoEncontradoException;
import com.nexus.contractflow.exception.RegraNegocioException;
import com.nexus.contractflow.mapper.ContratoMapper;
import com.nexus.contractflow.repository.ContratoRepository;
import com.nexus.contractflow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ContratoMapper contratoMapper;

    @Transactional
    public ContratoResponseDTO criar(ContratoRequestDTO dto) {
        validarVigencia(dto.getDataInicio(), dto.getDataFim());

        if (contratoRepository.existsByNumeroContrato(dto.getNumeroContrato())) {
            throw new RecursoDuplicadoException(
                    "Já existe um contrato com o número " + dto.getNumeroContrato() + ".");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> RecursoNaoEncontradoException.paraFornecedor(dto.getFornecedorId()));

        StatusContrato statusInicial = LocalDate.now().isAfter(dto.getDataFim())
                ? StatusContrato.VENCIDO
                : StatusContrato.ATIVO;

        Contrato contrato = Contrato.builder()
                .numeroContrato(dto.getNumeroContrato())
                .valorTotal(dto.getValorTotal().setScale(2, RoundingMode.HALF_UP))
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .status(statusInicial)
                .fornecedor(fornecedor)
                .build();

        Contrato salvo = contratoRepository.save(contrato);
        log.info("Contrato criado: numero={}, fornecedorId={}, valorTotal={}, status={}",
                salvo.getNumeroContrato(), fornecedor.getId(), salvo.getValorTotal(), salvo.getStatus());

        return contratoMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public ContratoResponseDTO buscarPorId(Long id) {
        Contrato contrato = obter(id);
        return contratoMapper.toResponse(aplicarStatusDinamico(contrato));
    }

    @Transactional(readOnly = true)
    public List<ContratoResponseDTO> buscarVencendoEm30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);
        List<Contrato> contratos = contratoRepository.findContratosVencendoNoPeriodo(
                StatusContrato.ATIVO, hoje, limite);
        return contratos.stream()
                .map(this::aplicarStatusDinamico)
                .map(contratoMapper::toResponse)
                .toList();
    }

    @Transactional
    public ContratoResponseDTO suspender(Long id, SuspenderContratoDTO dto) {
        Contrato contrato = obter(id);

        if (contrato.getStatus() == StatusContrato.CANCELADO) {
            throw new RegraNegocioException("Contratos CANCELADOS não podem ser suspensos.");
        }
        if (contrato.getStatus() == StatusContrato.SUSPENSO) {
            throw new RegraNegocioException("Este contrato já está SUSPENSO.");
        }

        StatusContrato statusAnterior = contrato.getStatus();
        contrato.setStatus(StatusContrato.SUSPENSO);
        Contrato salvo = contratoRepository.save(contrato);

        log.info("Contrato {} mudou de {} para SUSPENSO. Justificativa: '{}'",
                salvo.getNumeroContrato(), statusAnterior, dto.getJustificativa());

        return contratoMapper.toResponse(salvo);
    }

    public Contrato aplicarStatusDinamico(Contrato contrato) {
        if (contrato.getStatus() == StatusContrato.ATIVO
                && LocalDate.now().isAfter(contrato.getDataFim())) {
            log.debug("Status dinâmico aplicado em consulta: contrato {} marcado como VENCIDO em memória.",
                    contrato.getNumeroContrato());
            contrato.setStatus(StatusContrato.VENCIDO);
        }
        return contrato;
    }

    public BigDecimal somarValorContratosAtivos() {
        BigDecimal soma = contratoRepository.somarValorTotalPorStatus(StatusContrato.ATIVO);
        return (soma == null ? BigDecimal.ZERO : soma).setScale(2, RoundingMode.HALF_UP);
    }

    public long contarContratosAtivos() {
        return contratoRepository.countByStatus(StatusContrato.ATIVO);
    }

    public long contarVencendoEm30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);
        return contratoRepository.findContratosVencendoNoPeriodo(StatusContrato.ATIVO, hoje, limite).size();
    }

    private void validarVigencia(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("As datas de início e fim são obrigatórias.");
        }
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException(
                    "A data de fim (" + fim + ") não pode ser anterior à data de início (" + inicio + ").");
        }
    }

    private Contrato obter(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.paraContrato(id));
    }
}
