package com.nexus.contractflow.controller;

import com.nexus.contractflow.dto.request.ContratoRequestDTO;
import com.nexus.contractflow.dto.request.SuspenderContratoDTO;
import com.nexus.contractflow.dto.response.ContratoResponseDTO;
import com.nexus.contractflow.dto.response.ErrorResponse;
import com.nexus.contractflow.service.ContratoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contratos")
@RequiredArgsConstructor
@Tag(name = "Contratos", description = "Gestão de contratos e vigências")
@SecurityRequirement(name = "bearerAuth")
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    @Operation(summary = "Criar novo contrato vinculado a um fornecedor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrato criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Regra de vigência violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ContratoResponseDTO> criar(@Valid @RequestBody ContratoRequestDTO dto) {
        ContratoResponseDTO criado = contratoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/vencendo")
    @Operation(summary = "Listar contratos ATIVOS que expiram nos próximos 30 dias")
    public ResponseEntity<List<ContratoResponseDTO>> vencendoEm30Dias() {
        return ResponseEntity.ok(contratoService.buscarVencendoEm30Dias());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar contrato por ID (status calculado dinamicamente)")
    public ResponseEntity<ContratoResponseDTO> buscarPorId(
            @Parameter(description = "ID do contrato", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(contratoService.buscarPorId(id));
    }

    @PutMapping("/{id}/suspender")
    @Operation(summary = "Suspender contrato administrativamente",
            description = "Altera o status do contrato para SUSPENSO mediante justificativa.")
    public ResponseEntity<ContratoResponseDTO> suspender(
            @PathVariable Long id,
            @Valid @RequestBody SuspenderContratoDTO dto) {
        return ResponseEntity.ok(contratoService.suspender(id, dto));
    }
}
