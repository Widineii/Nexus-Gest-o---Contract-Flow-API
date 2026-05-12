package com.nexus.contractflow.controller;

import com.nexus.contractflow.dto.request.FornecedorRequestDTO;
import com.nexus.contractflow.dto.response.ContratoResponseDTO;
import com.nexus.contractflow.dto.response.ErrorResponse;
import com.nexus.contractflow.dto.response.FornecedorResponseDTO;
import com.nexus.contractflow.service.FornecedorService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
@Tag(name = "Fornecedores", description = "Gestão de fornecedores")
@SecurityRequirement(name = "bearerAuth")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    @Operation(summary = "Cadastrar novo fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fornecedor criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FornecedorResponseDTO> criar(@Valid @RequestBody FornecedorRequestDTO dto) {
        FornecedorResponseDTO criado = fornecedorService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar fornecedores com paginação")
    public ResponseEntity<Page<FornecedorResponseDTO>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nomeFantasia") Pageable pageable) {
        return ResponseEntity.ok(fornecedorService.listar(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(
            @Parameter(description = "ID do fornecedor", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/{id}/contratos")
    @Operation(summary = "Listar contratos de um fornecedor específico")
    public ResponseEntity<List<ContratoResponseDTO>> listarContratos(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.listarContratosDoFornecedor(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fornecedor",
            description = "Não é permitido excluir fornecedor com contratos ATIVOS.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Excluído"),
            @ApiResponse(responseCode = "409", description = "Possui contratos ativos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fornecedorService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
