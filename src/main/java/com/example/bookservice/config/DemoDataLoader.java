package com.example.bookservice.config;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Carga un catalogo de ejemplo al arrancar si la base de datos esta vacia.
 * H2 es en memoria, asi que en la demo publica cada reinicio vuelve a este estado inicial.
 * Pasa por BookService para reutilizar las mismas reglas de negocio (p.ej. ISBN duplicado).
 */
@Component
public class DemoDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    private final BookRepository bookRepository;
    private final BookService bookService;

    public DemoDataLoader(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }
        List.of(
                new BookRequest("Clean Code", "Robert C. Martin", "9780132350884", 2008),
                new BookRequest("Effective Java", "Joshua Bloch", "9780134685991", 2018),
                new BookRequest("Domain-Driven Design", "Eric Evans", "9780321125217", 2003),
                new BookRequest("Refactoring", "Martin Fowler", "9780134757599", 2018),
                new BookRequest("Designing Data-Intensive Applications", "Martin Kleppmann", "9781449373320", 2017),
                new BookRequest("Spring in Action", "Craig Walls", "9781617297571", 2022)
        ).forEach(bookService::create);
        log.info("Demo data loaded: {} books", bookRepository.count());
    }
}
