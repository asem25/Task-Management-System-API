package com.semavin.API.utils.exceptions;

import org.springframework.validation.FieldError;

import java.util.List;

public class CommentFieldsErrorException extends FieldErrorException{
    public CommentFieldsErrorException(List<FieldError> fieldErrorList) {
        super(fieldErrorList);
    }
}
