package com.vidaloca.skibidi.product.validation;


import com.vidaloca.skibidi.user.registration.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
@Documented
public @interface Price {
    String message() default "Price must be higher than 0 and format like 0.00";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}