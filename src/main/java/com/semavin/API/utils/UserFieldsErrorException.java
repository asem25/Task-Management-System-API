package com.semavin.API.utils;

import org.springframework.validation.FieldError;

import java.util.*;

public class UserFieldsErrorException extends FieldErrorException{
    public UserFieldsErrorException(List<FieldError> fieldErrorList) {
        super(fieldErrorList);
    }
}
