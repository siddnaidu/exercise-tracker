package com.siddnaidu.exercisetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExerciseNotFoundException extends  RuntimeException{
    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
