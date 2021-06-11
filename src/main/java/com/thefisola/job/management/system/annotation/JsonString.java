package com.thefisola.job.management.system.annotation;

import com.thefisola.job.management.system.annotation.impl.JsonStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JsonStringValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonString {
    String message() default "The String is not in JSON format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
