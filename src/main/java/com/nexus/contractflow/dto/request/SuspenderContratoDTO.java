package com.nexus.contractflow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para suspender contrato")
public class SuspenderContratoDTO {

    @NotBlank(message = "A justificativa é obrigatória.")
    @Size(min = 10, max = 500, message = "A justificativa deve ter entre 10 e 500 caracteres.")
    @Schema(example = "Suspensão temporária por reavaliação contratual.")
    private String justificativa;
}
