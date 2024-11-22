package com.semavin.API.utils;

import org.springframework.validation.FieldError;

import java.util.List;

public class TaskFieldsErrorException extends FieldErrorException{
    public TaskFieldsErrorException(List<FieldError> fieldErrorList) {
        super(fieldErrorList);
    }
}
