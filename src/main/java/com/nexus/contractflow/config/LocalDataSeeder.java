package com.nexus.contractflow.config;

import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.entity.Fornecedor;
import com.nexus.contractflow.entity.Usuario;
import com.nexus.contractflow.entity.enums.CategoriaFornecedor;
import com.nexus.contractflow.entity.enums.Role;
import com.nexus.contractflow.entity.enums.StatusContrato;
import com.nexus.contractflow.repository.ContratoRepository;
import com.nexus.contractflow.repository.FornecedorRepository;
import com.nexus.contractflow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Popula dados de exemplo quando o perfil "local" (H2 em memória) está ativo.
 * Mantém o "Flyway/seed.sql" do MySQL intacto e fornece um setup zero-config para demonstração.
 */
@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalDataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ContratoRepository contratoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("[seed-local] Banco já populado, pulando seed.");
            return;
        }

        log.info("[seed-local] Inserindo usuário admin e dados de exemplo (perfil local + H2)...");

        usuarioRepository.save(Usuario.builder()
                .nome("Administrador Nexus")
                .email("admin@nexus.com")
                .senha(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());

        Fornecedor f1 = fornecedorRepository.save(Fornecedor.builder()
                .nomeFantasia("TechSolutions Brasil")
                .cnpj("12345678000190")
                .email("contato@techsolutions.com.br")
                .categoria(CategoriaFornecedor.TI)
                .build());

        Fornecedor f2 = fornecedorRepository.save(Fornecedor.builder()
                .nomeFantasia("LimpaTudo Serviços")
                .cnpj("98765432000110")
                .email("comercial@limpatudo.com.br")
                .categoria(CategoriaFornecedor.LIMPEZA)
                .build());

        Fornecedor f3 = fornecedorRepository.save(Fornecedor.builder()
                .nomeFantasia("ManutençãoPro Ltda")
                .cnpj("45678912000155")
                .email("atendimento@manutencaopro.com.br")
                .categoria(CategoriaFornecedor.MANUTENCAO)
                .build());

        Fornecedor f4 = fornecedorRepository.save(Fornecedor.builder()
                .nomeFantasia("LogisticaExpress")
                .cnpj("78912345000133")
                .email("vendas@logisticaexpress.com.br")
                .categoria(CategoriaFornecedor.LOGISTICA)
                .build());

        LocalDate hoje = LocalDate.now();

        contratoRepository.saveAll(List.of(
                Contrato.builder()
                        .numeroContrato("CT-2026-001")
                        .valorTotal(new BigDecimal("120000.00"))
                        .dataInicio(hoje.minusMonths(3))
                        .dataFim(hoje.plusMonths(9))
                        .status(StatusContrato.ATIVO)
                        .fornecedor(f1)
                        .build(),
                Contrato.builder()
                        .numeroContrato("CT-2026-002")
                        .valorTotal(new BigDecimal("48000.00"))
                        .dataInicio(hoje.minusMonths(2))
                        .dataFim(hoje.plusMonths(10))
                        .status(StatusContrato.ATIVO)
                        .fornecedor(f2)
                        .build(),
                Contrato.builder()
                        .numeroContrato("CT-2026-003")
                        .valorTotal(new BigDecimal("86500.50"))
                        .dataInicio(hoje.minusMonths(1))
                        .dataFim(hoje.plusDays(20))
                        .status(StatusContrato.ATIVO)
                        .fornecedor(f3)
                        .build(),
                Contrato.builder()
                        .numeroContrato("CT-2025-099")
                        .valorTotal(new BigDecimal("35000.00"))
                        .dataInicio(hoje.minusYears(1))
                        .dataFim(hoje.minusMonths(2))
                        .status(StatusContrato.VENCIDO)
                        .fornecedor(f4)
                        .build(),
                Contrato.builder()
                        .numeroContrato("CT-2026-004")
                        .valorTotal(new BigDecimal("72000.00"))
                        .dataInicio(hoje.minusMonths(1))
                        .dataFim(hoje.plusMonths(5))
                        .status(StatusContrato.SUSPENSO)
                        .fornecedor(f1)
                        .build()
        ));

        log.info("[seed-local] Seed concluído: 1 admin, {} fornecedores, {} contratos.",
                fornecedorRepository.count(), contratoRepository.count());
    }
}
