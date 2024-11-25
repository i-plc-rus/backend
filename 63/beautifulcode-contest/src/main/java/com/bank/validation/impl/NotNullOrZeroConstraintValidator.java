package com.bank.validation.impl;

import com.bank.validation.NotNullOrZeroConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullOrZeroConstraintValidator implements ConstraintValidator<NotNullOrZeroConstraint, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value != null && value != 0;
    }

    @Override
    public void initialize(NotNullOrZeroConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
