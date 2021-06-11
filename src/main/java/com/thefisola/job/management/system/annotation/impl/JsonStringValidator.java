package com.thefisola.job.management.system.annotation.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thefisola.job.management.system.annotation.JsonString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class JsonStringValidator implements ConstraintValidator<JsonString, String> {

    @Override
    public void initialize(JsonString jsonString) {
        // do nothing
    }

    @Override
    public boolean isValid(String jsonString, ConstraintValidatorContext context) {
        try {
            new ObjectMapper().readValue(jsonString, Object.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
