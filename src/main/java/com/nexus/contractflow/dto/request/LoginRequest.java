package com.nexus.contractflow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciais para login")
public class LoginRequest {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Schema(example = "admin@nexus.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Schema(example = "admin123")
    private String senha;
}
