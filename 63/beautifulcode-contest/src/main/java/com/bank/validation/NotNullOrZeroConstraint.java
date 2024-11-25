package com.bank.validation;

import com.bank.validation.impl.NotNullOrZeroConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кастомная валидация для отклонения нулевых транзакций.
 */
@Constraint(validatedBy = NotNullOrZeroConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullOrZeroConstraint {
    String message() default "Transaction amount cannot be zero";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
