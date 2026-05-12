package com.nexus.contractflow.exception;

import com.nexus.contractflow.dto.response.ErrorResponse;
import com.nexus.contractflow.dto.response.ErrorResponse.FieldErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNaoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), req, null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErrorResponse> handleRegraNegocio(RegraNegocioException ex, HttpServletRequest req) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negócio violada", ex.getMessage(), req, null);
    }

    @ExceptionHandler(FornecedorComContratosAtivosException.class)
    public ResponseEntity<ErrorResponse> handleFornecedorComContratos(FornecedorComContratosAtivosException ex, HttpServletRequest req) {
        log.warn("Conflito de integridade: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "Conflito de integridade", ex.getMessage(), req, null);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleDuplicado(RecursoDuplicadoException ex, HttpServletRequest req) {
        log.warn("Recurso duplicado: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "Recurso duplicado", ex.getMessage(), req, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String mensagem = "Violação de integridade no banco de dados.";
        String raiz = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (raiz != null && raiz.toLowerCase().contains("cnpj")) {
            mensagem = "Já existe um fornecedor cadastrado com este CNPJ.";
        } else if (raiz != null && raiz.toLowerCase().contains("numero_contrato")) {
            mensagem = "Já existe um contrato com este número.";
        } else if (raiz != null && raiz.toLowerCase().contains("email")) {
            mensagem = "Já existe um registro com este e-mail.";
        }
        log.warn("Violação de integridade: {}", raiz);
        return build(HttpStatus.CONFLICT, "Conflito de integridade", mensagem, req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldErrorDetail> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldErrorDetail)
                .toList();
        log.warn("Erro de validação em {}: {}", req.getRequestURI(), detalhes);
        return build(HttpStatus.BAD_REQUEST, "Erro de validação",
                "Um ou mais campos estão inválidos.", req, detalhes);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String mensagem = "Valor inválido para o parâmetro '" + ex.getName() + "'.";
        log.warn("Type mismatch: {}", mensagem);
        return build(HttpStatus.BAD_REQUEST, "Parâmetro inválido", mensagem, req, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        log.warn("Falha de autenticação em {}: credenciais inválidas", req.getRequestURI());
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado",
                "E-mail ou senha inválidos.", req, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        log.warn("Falha de autenticação em {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado",
                "Autenticação necessária para acessar este recurso.", req, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("Acesso negado em {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.FORBIDDEN, "Acesso negado",
                "Você não tem permissão para acessar este recurso.", req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Erro não tratado em {}: ", req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", req, null);
    }

    private FieldErrorDetail toFieldErrorDetail(FieldError fe) {
        return FieldErrorDetail.builder()
                .campo(fe.getField())
                .mensagem(fe.getDefaultMessage())
                .build();
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message,
                                                HttpServletRequest req, List<FieldErrorDetail> details) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(req.getRequestURI())
                .details(details)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
