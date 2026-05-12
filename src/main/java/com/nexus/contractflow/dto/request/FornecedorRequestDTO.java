package com.nexus.contractflow.dto.request;

import com.nexus.contractflow.entity.enums.CategoriaFornecedor;
import com.nexus.contractflow.validation.CNPJ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para cadastro/atualização de fornecedor")
public class FornecedorRequestDTO {

    @NotBlank(message = "O nome fantasia é obrigatório.")
    @Size(max = 150)
    @Schema(example = "TechSolutions Brasil", description = "Nome comercial do fornecedor")
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @CNPJ
    @Schema(example = "12345678000190", description = "CNPJ com 14 dígitos numéricos")
    private String cnpj;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150)
    @Schema(example = "contato@techsolutions.com.br")
    private String email;

    @NotNull(message = "A categoria é obrigatória.")
    @Schema(example = "TI", description = "Categoria do fornecedor")
    private CategoriaFornecedor categoria;
}
