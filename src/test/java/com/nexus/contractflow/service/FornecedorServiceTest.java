package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.request.FornecedorRequestDTO;
import com.nexus.contractflow.entity.Fornecedor;
import com.nexus.contractflow.entity.enums.CategoriaFornecedor;
import com.nexus.contractflow.entity.enums.StatusContrato;
import com.nexus.contractflow.exception.FornecedorComContratosAtivosException;
import com.nexus.contractflow.exception.RecursoDuplicadoException;
import com.nexus.contractflow.mapper.ContratoMapper;
import com.nexus.contractflow.mapper.FornecedorMapper;
import com.nexus.contractflow.repository.ContratoRepository;
import com.nexus.contractflow.repository.FornecedorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock private FornecedorRepository fornecedorRepository;
    @Mock private ContratoRepository contratoRepository;
    @Mock private FornecedorMapper fornecedorMapper;
    @Mock private ContratoMapper contratoMapper;
    @Mock private ContratoService contratoService;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    @DisplayName("Deve impedir cadastro de fornecedor com CNPJ já existente")
    void deveImpedirCnpjDuplicado() {
        FornecedorRequestDTO dto = FornecedorRequestDTO.builder()
                .nomeFantasia("Empresa X")
                .cnpj("12345678000190")
                .email("contato@x.com")
                .categoria(CategoriaFornecedor.TI)
                .build();

        when(fornecedorRepository.existsByCnpj("12345678000190")).thenReturn(true);

        assertThatThrownBy(() -> fornecedorService.criar(dto))
                .isInstanceOf(RecursoDuplicadoException.class)
                .hasMessageContaining("Já existe um fornecedor cadastrado com o CNPJ");
    }

    @Test
    @DisplayName("Deve bloquear exclusão de fornecedor com contratos ATIVOS")
    void deveBloquearExclusaoComContratosAtivos() {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(1L)
                .nomeFantasia("Empresa Y")
                .cnpj("98765432000110")
                .email("y@y.com")
                .categoria(CategoriaFornecedor.LIMPEZA)
                .build();

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(contratoRepository.existsByFornecedorIdAndStatus(1L, StatusContrato.ATIVO)).thenReturn(true);

        assertThatThrownBy(() -> fornecedorService.excluir(1L))
                .isInstanceOf(FornecedorComContratosAtivosException.class)
                .hasMessageContaining("contratos ATIVOS");
    }
}
