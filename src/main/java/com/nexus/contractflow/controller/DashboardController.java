package com.nexus.contractflow.controller;

import com.nexus.contractflow.dto.response.DashboardResponseDTO;
import com.nexus.contractflow.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Indicadores agregados do sistema")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/resumo")
    @Operation(summary = "Resumo geral",
            description = "Retorna valor total investido em contratos ATIVOS, total de fornecedores, total de contratos ativos e quantos estão vencendo em 30 dias.")
    public ResponseEntity<DashboardResponseDTO> resumo() {
        return ResponseEntity.ok(dashboardService.obterResumo());
    }
}
