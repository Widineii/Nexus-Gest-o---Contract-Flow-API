package com.nexus.contractflow.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CNPJValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {

    String message() default "CNPJ inválido. Informe 14 dígitos numéricos válidos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
