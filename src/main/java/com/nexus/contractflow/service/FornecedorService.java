package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.request.FornecedorRequestDTO;
import com.nexus.contractflow.dto.response.ContratoResponseDTO;
import com.nexus.contractflow.dto.response.FornecedorResponseDTO;
import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.entity.Fornecedor;
import com.nexus.contractflow.entity.enums.StatusContrato;
import com.nexus.contractflow.exception.FornecedorComContratosAtivosException;
import com.nexus.contractflow.exception.RecursoDuplicadoException;
import com.nexus.contractflow.exception.RecursoNaoEncontradoException;
import com.nexus.contractflow.mapper.ContratoMapper;
import com.nexus.contractflow.mapper.FornecedorMapper;
import com.nexus.contractflow.repository.ContratoRepository;
import com.nexus.contractflow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ContratoRepository contratoRepository;
    private final FornecedorMapper fornecedorMapper;
    private final ContratoMapper contratoMapper;
    private final ContratoService contratoService;

    @Transactional
    public FornecedorResponseDTO criar(FornecedorRequestDTO dto) {
        String cnpjLimpo = limparCnpj(dto.getCnpj());

        if (fornecedorRepository.existsByCnpj(cnpjLimpo)) {
            log.warn("Tentativa de cadastrar fornecedor com CNPJ duplicado: {}", cnpjLimpo);
            throw new RecursoDuplicadoException(
                    "Já existe um fornecedor cadastrado com o CNPJ " + cnpjLimpo + ".");
        }

        Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
        fornecedor.setCnpj(cnpjLimpo);

        Fornecedor salvo = fornecedorRepository.save(fornecedor);
        log.info("Fornecedor criado: id={}, nomeFantasia='{}', cnpj={}",
                salvo.getId(), salvo.getNomeFantasia(), salvo.getCnpj());

        return fornecedorMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponseDTO> listar(Pageable pageable) {
        return fornecedorRepository.findAll(pageable).map(fornecedorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(Long id) {
        Fornecedor fornecedor = obter(id);
        return fornecedorMapper.toResponse(fornecedor);
    }

    @Transactional(readOnly = true)
    public List<ContratoResponseDTO> listarContratosDoFornecedor(Long fornecedorId) {
        if (!fornecedorRepository.existsById(fornecedorId)) {
            throw RecursoNaoEncontradoException.paraFornecedor(fornecedorId);
        }
        List<Contrato> contratos = contratoRepository.findByFornecedorId(fornecedorId);
        return contratos.stream()
                .map(contratoService::aplicarStatusDinamico)
                .map(contratoMapper::toResponse)
                .toList();
    }

    @Transactional
    public void excluir(Long id) {
        Fornecedor fornecedor = obter(id);

        boolean possuiAtivos = contratoRepository.existsByFornecedorIdAndStatus(id, StatusContrato.ATIVO);
        if (possuiAtivos) {
            log.warn("Tentativa de excluir fornecedor {} com contratos ATIVOS vinculados.", id);
            throw new FornecedorComContratosAtivosException(id);
        }

        List<Contrato> contratosHistoricos = contratoRepository.findByFornecedorId(id);
        if (!contratosHistoricos.isEmpty()) {
            log.info("Excluindo {} contrato(s) histórico(s) do fornecedor {}.", contratosHistoricos.size(), id);
            contratoRepository.deleteAll(contratosHistoricos);
        }

        fornecedorRepository.delete(fornecedor);
        log.info("Fornecedor excluído: id={}, nomeFantasia='{}'", id, fornecedor.getNomeFantasia());
    }

    public Fornecedor obter(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.paraFornecedor(id));
    }

    private String limparCnpj(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }
}
