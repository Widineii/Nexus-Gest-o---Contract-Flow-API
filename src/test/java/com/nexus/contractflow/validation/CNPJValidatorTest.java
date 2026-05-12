package com.nexus.contractflow.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CNPJValidatorTest {

    private final CNPJValidator validator = new CNPJValidator();

    @Test
    @DisplayName("CNPJ válido deve passar")
    void cnpjValido() {
        assertThat(validator.isValid("11222333000181", null)).isTrue();
    }

    @Test
    @DisplayName("CNPJ com 14 dígitos repetidos deve falhar")
    void cnpjDigitosRepetidos() {
        assertThat(validator.isValid("11111111111111", null)).isFalse();
    }

    @Test
    @DisplayName("CNPJ com menos de 14 dígitos deve falhar")
    void cnpjCurto() {
        assertThat(validator.isValid("12345678", null)).isFalse();
    }

    @Test
    @DisplayName("CNPJ com dígito verificador inválido deve falhar")
    void cnpjDigitoVerificadorInvalido() {
        assertThat(validator.isValid("12345678000100", null)).isFalse();
    }

    @Test
    @DisplayName("CNPJ nulo deve falhar")
    void cnpjNulo() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
