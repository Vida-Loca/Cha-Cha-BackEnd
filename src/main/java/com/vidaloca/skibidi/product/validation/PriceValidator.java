package com.vidaloca.skibidi.product.validation;

import com.vidaloca.skibidi.user.registration.validation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceValidator
        implements ConstraintValidator<Price, BigDecimal> {

    private static final int PRECISION = 2;
    @Override
    public void initialize(Price constraintAnnotation) {
    }
    @Override
    public boolean isValid(BigDecimal price, ConstraintValidatorContext context){

        return (validatePrice(price));
    }
    private boolean validatePrice(BigDecimal price) {
        if ( price.compareTo(new BigDecimal("0.0")) < 0)
            return false;
        return price.scale() <= 2;
    }
}