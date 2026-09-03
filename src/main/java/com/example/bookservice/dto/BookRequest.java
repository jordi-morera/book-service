package com.example.bookservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO de entrada. Record de Java 25: inmutable, sin logica, solo transporte de datos validados.
 * El patron ISBN acepta formato ISBN-10 o ISBN-13 con guiones opcionales.
 */
public record BookRequest(

        @NotBlank(message = "El titulo es obligatorio")
        String title,

        @NotBlank(message = "El autor es obligatorio")
        String author,

        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "^(97[89])?\\d{9}(\\d|X)$|^[\\d-]{10,17}$", message = "El ISBN no tiene un formato valido")
        String isbn,

        @Min(value = 1450, message = "El anio de publicacion no es valido")
        @Max(value = 2100, message = "El anio de publicacion no es valido")
        Integer publishedYear
) {
}
