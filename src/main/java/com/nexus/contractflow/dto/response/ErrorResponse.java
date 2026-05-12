package com.nexus.contractflow.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Padrão de resposta para erros da API")
public class ErrorResponse {

    @Schema(example = "2026-05-12T18:05:30")
    private LocalDateTime timestamp;

    @Schema(example = "404")
    private Integer status;

    @Schema(example = "Recurso não encontrado")
    private String error;

    @Schema(example = "Fornecedor não encontrado com o ID 99")
    private String message;

    @Schema(example = "/api/v1/fornecedores/99")
    private String path;

    @Schema(description = "Lista de erros de validação por campo, quando aplicável")
    private List<FieldErrorDetail> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Detalhe de um erro de validação em campo")
    public static class FieldErrorDetail {
        @Schema(example = "cnpj")
        private String campo;

        @Schema(example = "CNPJ inválido. Informe 14 dígitos numéricos válidos.")
        private String mensagem;
    }
}
