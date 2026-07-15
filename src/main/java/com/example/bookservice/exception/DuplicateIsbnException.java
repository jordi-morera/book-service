package com.example.bookservice.exception;

public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("Ya existe un libro registrado con ISBN=" + isbn);
    }
}
