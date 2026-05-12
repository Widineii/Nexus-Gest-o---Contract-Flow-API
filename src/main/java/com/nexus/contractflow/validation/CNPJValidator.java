package com.nexus.contractflow.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) {
            return false;
        }

        String somenteDigitos = cnpj.replaceAll("\\D", "");

        if (somenteDigitos.length() != 14) {
            return false;
        }

        if (somenteDigitos.matches("^(\\d)\\1{13}$")) {
            return false;
        }

        try {
            int digito1 = calcularDigito(somenteDigitos.substring(0, 12), new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            int digito2 = calcularDigito(somenteDigitos.substring(0, 12) + digito1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

            return Character.getNumericValue(somenteDigitos.charAt(12)) == digito1
                    && Character.getNumericValue(somenteDigitos.charAt(13)) == digito2;
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
