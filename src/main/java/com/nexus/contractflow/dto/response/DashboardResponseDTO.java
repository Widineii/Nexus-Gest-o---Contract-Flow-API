package com.nexus.contractflow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumo geral do sistema para dashboard")
public class DashboardResponseDTO {

    @Schema(example = "375500.50", description = "Soma do valor total dos contratos ATIVOS")
    private BigDecimal valorTotalContratosAtivos;

    @Schema(example = "4", description = "Quantidade total de fornecedores cadastrados")
    private Long totalFornecedores;

    @Schema(example = "3", description = "Quantidade total de contratos ATIVOS")
    private Long totalContratosAtivos;

    @Schema(example = "1", description = "Quantidade de contratos vencendo nos próximos 30 dias")
    private Long contratosVencendoEm30Dias;
}
