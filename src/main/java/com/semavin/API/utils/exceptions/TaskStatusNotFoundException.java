package com.semavin.API.utils.exceptions;

public class TaskStatusNotFoundException extends RuntimeException{
    public TaskStatusNotFoundException(String message) {
        super(message);
    }
}
