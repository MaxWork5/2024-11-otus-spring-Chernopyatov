package ru.otus.project.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.project.exceptions.ClientNotFoundException;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.exceptions.ReviewNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ClientNotFoundException.class,
            EventNotFoundException.class,
            OrganizerNotFoundException.class,
            ReviewNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
