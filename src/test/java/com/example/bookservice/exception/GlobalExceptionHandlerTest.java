package com.example.bookservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesNotFound() {
        when(request.getRequestURI()).thenReturn("/api/v1/books/7");

        var response = handler.handleNotFound(new BookNotFoundException(7L), request);
        var body = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(body.message()).contains("id=7");
        assertThat(body.path()).isEqualTo("/api/v1/books/7");
    }

    @Test
    void handlesDuplicateIsbn() {
        when(request.getRequestURI()).thenReturn("/api/v1/books");

        var response = handler.handleDuplicate(new DuplicateIsbnException("1234567890"), request);
        var body = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(body.message()).contains("1234567890");
    }

    @Test
    void handlesAccessDenied() {
        when(request.getRequestURI()).thenReturn("/api/v1/books");

        var response = handler.handleAccessDenied(new AccessDeniedException("denied"), request);
        var body = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(body.details()).isEmpty();
    }

    @Test
    void handlesGenericException() {
        when(request.getRequestURI()).thenReturn("/api/v1/books");

        var response = handler.handleGeneric(new IllegalStateException("failure"), request);
        var body = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body.message()).isEqualTo("Ha ocurrido un error inesperado");
    }
}
