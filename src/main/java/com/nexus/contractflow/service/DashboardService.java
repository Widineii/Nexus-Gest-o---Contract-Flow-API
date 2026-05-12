package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.response.DashboardResponseDTO;
import com.nexus.contractflow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FornecedorRepository fornecedorRepository;
    private final ContratoService contratoService;

    @Transactional(readOnly = true)
    public DashboardResponseDTO obterResumo() {
        return DashboardResponseDTO.builder()
                .valorTotalContratosAtivos(contratoService.somarValorContratosAtivos())
                .totalFornecedores(fornecedorRepository.count())
                .totalContratosAtivos(contratoService.contarContratosAtivos())
                .contratosVencendoEm30Dias(contratoService.contarVencendoEm30Dias())
                .build();
    }
}
