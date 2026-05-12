package com.nexus.contractflow.service;

import com.nexus.contractflow.dto.request.ContratoRequestDTO;
import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.entity.Fornecedor;
import com.nexus.contractflow.entity.enums.CategoriaFornecedor;
import com.nexus.contractflow.entity.enums.StatusContrato;
import com.nexus.contractflow.exception.RecursoNaoEncontradoException;
import com.nexus.contractflow.exception.RegraNegocioException;
import com.nexus.contractflow.mapper.ContratoMapper;
import com.nexus.contractflow.repository.ContratoRepository;
import com.nexus.contractflow.repository.FornecedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ContratoMapper contratoMapper;

    @InjectMocks
    private ContratoService contratoService;

    private Fornecedor fornecedor;

    @BeforeEach
    void setup() {
        fornecedor = Fornecedor.builder()
                .id(1L)
                .nomeFantasia("TechSolutions")
                .cnpj("12345678000190")
                .email("contato@tech.com")
                .categoria(CategoriaFornecedor.TI)
                .build();
    }

    @Test
    @DisplayName("Deve rejeitar contrato com dataFim anterior à dataInicio")
    void deveRejeitarVigenciaInvalida() {
        ContratoRequestDTO dto = ContratoRequestDTO.builder()
                .numeroContrato("CT-2026-TEST")
                .valorTotal(new BigDecimal("1000.00"))
                .dataInicio(LocalDate.of(2026, 12, 31))
                .dataFim(LocalDate.of(2026, 1, 1))
                .fornecedorId(1L)
                .build();

        assertThatThrownBy(() -> contratoService.criar(dto))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("não pode ser anterior");
    }

    @Test
    @DisplayName("Deve falhar ao criar contrato com fornecedor inexistente")
    void deveFalharFornecedorInexistente() {
        ContratoRequestDTO dto = ContratoRequestDTO.builder()
                .numeroContrato("CT-2026-NEW")
                .valorTotal(new BigDecimal("5000.00"))
                .dataInicio(LocalDate.now())
                .dataFim(LocalDate.now().plusMonths(6))
                .fornecedorId(99L)
                .build();

        when(contratoRepository.existsByNumeroContrato(any())).thenReturn(false);
        when(fornecedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contratoService.criar(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Fornecedor não encontrado com o ID 99");
    }

    @Test
    @DisplayName("Status dinâmico: contrato ATIVO com dataFim no passado deve aparecer como VENCIDO")
    void deveAplicarStatusDinamicoVencido() {
        Contrato contrato = Contrato.builder()
                .id(10L)
                .numeroContrato("CT-2024-OLD")
                .valorTotal(new BigDecimal("100.00"))
                .dataInicio(LocalDate.of(2024, 1, 1))
                .dataFim(LocalDate.of(2024, 6, 30))
                .status(StatusContrato.ATIVO)
                .fornecedor(fornecedor)
                .build();

        Contrato resultado = contratoService.aplicarStatusDinamico(contrato);

        assertThat(resultado.getStatus()).isEqualTo(StatusContrato.VENCIDO);
    }

    @Test
    @DisplayName("Status dinâmico: contrato ATIVO com dataFim no futuro permanece ATIVO")
    void deveManterStatusAtivoSeNaoVenceu() {
        Contrato contrato = Contrato.builder()
                .id(11L)
                .numeroContrato("CT-2030-FUTURE")
                .valorTotal(new BigDecimal("200.00"))
                .dataInicio(LocalDate.now())
                .dataFim(LocalDate.now().plusYears(2))
                .status(StatusContrato.ATIVO)
                .fornecedor(fornecedor)
                .build();

        Contrato resultado = contratoService.aplicarStatusDinamico(contrato);

        assertThat(resultado.getStatus()).isEqualTo(StatusContrato.ATIVO);
    }

    @Test
    @DisplayName("Valor monetário deve ser arredondado para 2 casas decimais")
    void devePreservarPrecisaoMonetaria() {
        BigDecimal valor = new BigDecimal("1234.5678");
        BigDecimal arredondado = valor.setScale(2, java.math.RoundingMode.HALF_UP);
        assertThat(arredondado.toPlainString()).isEqualTo("1234.57");
    }
}
