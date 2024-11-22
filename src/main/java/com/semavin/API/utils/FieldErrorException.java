package com.semavin.API.utils;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class FieldErrorException extends RuntimeException {
    private final List<FieldError> fieldErrors;

    public FieldErrorException(List<FieldError> fieldErrorList) {
        super("Error in field/s: " + fieldErrorList.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "))); // Формируем сообщение для RuntimeException
        this.fieldErrors = fieldErrorList;
    }

    public List<String> getFieldNames() {
        return fieldErrors.stream()
                .map(FieldError::getField)
                .toList();
    }
    public List<String> getErrorMessages() {
        return fieldErrors.stream()
                .map(error -> String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
