package com.nexus.contractflow.repository;

import com.nexus.contractflow.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<Fornecedor> findByCnpj(String cnpj);
}
