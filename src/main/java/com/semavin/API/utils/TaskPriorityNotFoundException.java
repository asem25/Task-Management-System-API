package com.semavin.API.utils;

public class TaskPriorityNotFoundException extends RuntimeException{
    public TaskPriorityNotFoundException(String message) {
        super(message);
    }
}
