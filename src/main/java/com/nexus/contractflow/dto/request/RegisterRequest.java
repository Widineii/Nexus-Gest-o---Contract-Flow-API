package com.nexus.contractflow.dto.request;

import com.nexus.contractflow.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Dados para registro de novo usuário")
public class RegisterRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150)
    @Schema(example = "Maria Silva")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Schema(example = "maria.silva@nexus.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres.")
    @Schema(example = "senhaSegura123")
    private String senha;

    @Schema(example = "USER", description = "Role do usuário (ADMIN ou USER). Padrão: USER")
    private Role role;
}
