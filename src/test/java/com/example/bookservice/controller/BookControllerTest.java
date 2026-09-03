package com.example.bookservice.controller;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private final BookResponse response = new BookResponse(1L, "Title", "Author", "1234567890", 2020);
    private final BookRequest request = new BookRequest("Title", "Author", "1234567890", 2020);

    @Test
    void findAllReturnsOkWithBooks() {
        when(bookService.findAll()).thenReturn(List.of(response));

        var result = bookController.findAll();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void findByIdReturnsOkWithBook() {
        when(bookService.findById(1L)).thenReturn(response);

        var result = bookController.findById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void createReturnsCreatedLocationAndBody() {
        when(bookService.create(request)).thenReturn(response);

        var result = bookController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getHeaders().getLocation()).hasToString("/api/v1/books/1");
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void updateReturnsOkWithUpdatedBook() {
        when(bookService.update(1L, request)).thenReturn(response);

        var result = bookController.update(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void deleteReturnsNoContent() {
        var result = bookController.delete(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(bookService).delete(1L);
    }
}
