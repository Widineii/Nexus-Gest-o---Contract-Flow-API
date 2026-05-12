package com.nexus.contractflow.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }

    public static RecursoNaoEncontradoException paraFornecedor(Long id) {
        return new RecursoNaoEncontradoException("Fornecedor não encontrado com o ID " + id + ".");
    }

    public static RecursoNaoEncontradoException paraContrato(Long id) {
        return new RecursoNaoEncontradoException("Contrato não encontrado com o ID " + id + ".");
    }
}
