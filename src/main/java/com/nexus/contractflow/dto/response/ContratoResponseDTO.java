package com.nexus.contractflow.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexus.contractflow.entity.enums.StatusContrato;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação de um contrato")
public class ContratoResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "CT-2026-001")
    private String numeroContrato;

    @Schema(example = "120000.00")
    private BigDecimal valorTotal;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2026-01-15", type = "string", format = "date")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2026-12-31", type = "string", format = "date")
    private LocalDate dataFim;

    @Schema(example = "ATIVO", description = "Status atual (calculado dinamicamente em consultas)")
    private StatusContrato status;

    @Schema(example = "1")
    private Long fornecedorId;

    @Schema(example = "TechSolutions Brasil")
    private String fornecedorNome;

    @Schema(example = "2026-05-12T10:15:30")
    private LocalDateTime criadoEm;
}
