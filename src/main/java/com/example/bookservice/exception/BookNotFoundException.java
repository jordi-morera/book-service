package com.example.bookservice.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("No se encontro ningun libro con id=" + id);
    }
}
