package com.thefisola.job.management.system.exception.handler.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
public class ErrorResponse implements Serializable {
    private final boolean status = false;
    private final String message;
    private final Map<String, String> validation;
}
