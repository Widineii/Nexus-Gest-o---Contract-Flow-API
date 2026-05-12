package com.nexus.contractflow.dto.response;

import com.nexus.contractflow.entity.enums.Role;
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
@Schema(description = "Resposta de autenticação contendo o token JWT")
public class AuthResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(example = "Bearer")
    private String tipo;

    @Schema(example = "admin@nexus.com")
    private String email;

    @Schema(example = "ADMIN")
    private Role role;

    @Schema(example = "2026-05-13T18:05:30")
    private LocalDateTime expiresAt;
}
