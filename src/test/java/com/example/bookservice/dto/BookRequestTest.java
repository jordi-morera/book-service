package com.example.bookservice.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRequestTest {

    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void acceptsValidBookRequest() {
        BookRequest request = new BookRequest("Title", "Author", "9780132350884", 2020);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void rejectsBlankRequiredFieldsAndInvalidYear() {
        BookRequest request = new BookRequest("", "", "", 2101);

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("El titulo es obligatorio", "El autor es obligatorio", "El ISBN es obligatorio",
                        "El ISBN no tiene un formato valido", "El anio de publicacion no es valido");
    }
}
