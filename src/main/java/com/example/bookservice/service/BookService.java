package com.example.bookservice.service;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;

import java.util.List;

/**
 * Contrato del servicio de libros. El controlador depende de esta abstraccion
 * (Dependency Inversion Principle), no de la implementacion concreta.
 */
public interface BookService {

    List<BookResponse> findAll();

    BookResponse findById(Long id);

    BookResponse create(BookRequest request);

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);
}
