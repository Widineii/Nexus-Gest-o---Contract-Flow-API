package com.nexus.contractflow.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação de contrato")
public class ContratoRequestDTO {

    @NotBlank(message = "O número do contrato é obrigatório.")
    @Size(max = 20)
    @Schema(example = "CT-2026-001")
    private String numeroContrato;

    @NotNull(message = "O valor total é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero.")
    @Digits(integer = 13, fraction = 2, message = "O valor total deve ter no máximo 13 dígitos inteiros e 2 decimais.")
    @Schema(example = "120000.00")
    private BigDecimal valorTotal;

    @NotNull(message = "A data de início é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2026-01-15", type = "string", format = "date")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2026-12-31", type = "string", format = "date")
    private LocalDate dataFim;

    @NotNull(message = "O ID do fornecedor é obrigatório.")
    @Schema(example = "1", description = "ID do fornecedor existente vinculado ao contrato")
    private Long fornecedorId;

    @AssertTrue(message = "A data de fim não pode ser anterior à data de início.")
    @Schema(hidden = true)
    public boolean isVigenciaValida() {
        if (dataInicio == null || dataFim == null) {
            return true;
        }
        return !dataFim.isBefore(dataInicio);
    }
}
