package com.semavin.API.utils;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message) {
        super(message);
    }
}
