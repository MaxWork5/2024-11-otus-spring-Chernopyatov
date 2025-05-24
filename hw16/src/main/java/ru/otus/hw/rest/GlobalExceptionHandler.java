package ru.otus.hw.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handeIllegalArgumentException() {
        return ResponseEntity.status(400).body("Cannot transfer a book without specifying the genres");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handeNotFoundException() {
        return ResponseEntity.status(404).body("The books was not found");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handeEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}