package com.example.bookservice.dto;

import com.example.bookservice.entity.Book;

/**
 * DTO de salida. Evita exponer la entidad JPA directamente en la API (desacoplamiento capa web / persistencia).
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publishedYear
) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }
}
