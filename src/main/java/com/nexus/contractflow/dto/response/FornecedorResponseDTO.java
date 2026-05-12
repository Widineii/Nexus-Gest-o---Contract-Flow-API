package com.nexus.contractflow.dto.response;

import com.nexus.contractflow.entity.enums.CategoriaFornecedor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação de um fornecedor")
public class FornecedorResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "TechSolutions Brasil")
    private String nomeFantasia;

    @Schema(example = "12345678000190")
    private String cnpj;

    @Schema(example = "contato@techsolutions.com.br")
    private String email;

    @Schema(example = "TI")
    private CategoriaFornecedor categoria;

    @Schema(example = "2026-05-12T10:15:30")
    private LocalDateTime criadoEm;
}
