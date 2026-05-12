package com.nexus.contractflow.exception;

public class FornecedorComContratosAtivosException extends RuntimeException {

    public FornecedorComContratosAtivosException(Long fornecedorId) {
        super("Não é possível excluir o fornecedor ID " + fornecedorId
                + " porque ele possui contratos ATIVOS vinculados.");
    }
}
