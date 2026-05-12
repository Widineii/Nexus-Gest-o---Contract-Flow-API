package com.nexus.contractflow.repository;

import com.nexus.contractflow.entity.Contrato;
import com.nexus.contractflow.entity.enums.StatusContrato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    boolean existsByNumeroContrato(String numeroContrato);

    boolean existsByFornecedorIdAndStatus(Long fornecedorId, StatusContrato status);

    Page<Contrato> findByFornecedorId(Long fornecedorId, Pageable pageable);

    List<Contrato> findByFornecedorId(Long fornecedorId);

    @Query("SELECT c FROM Contrato c WHERE c.status = :status AND c.dataFim BETWEEN :inicio AND :fim")
    List<Contrato> findContratosVencendoNoPeriodo(
            @Param("status") StatusContrato status,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM Contrato c WHERE c.status = :status")
    BigDecimal somarValorTotalPorStatus(@Param("status") StatusContrato status);

    long countByStatus(StatusContrato status);

    @Modifying
    @Query("UPDATE Contrato c SET c.status = com.nexus.contractflow.entity.enums.StatusContrato.VENCIDO, c.atualizadoEm = CURRENT_TIMESTAMP "
            + "WHERE c.status = com.nexus.contractflow.entity.enums.StatusContrato.ATIVO AND c.dataFim < :hoje")
    int atualizarContratosVencidos(@Param("hoje") LocalDate hoje);

    @Query("SELECT c FROM Contrato c WHERE c.status = com.nexus.contractflow.entity.enums.StatusContrato.ATIVO AND c.dataFim < :hoje")
    List<Contrato> buscarAtivosComDataFimMenorQue(@Param("hoje") LocalDate hoje);
}
