package ru.otus.hw.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Book not found");
    }
}