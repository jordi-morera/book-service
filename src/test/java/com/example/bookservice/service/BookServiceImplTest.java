package com.example.bookservice.service;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.exception.DuplicateIsbnException;
import com.example.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findAllMapsBooksToResponses() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponse> responses = bookService.findAll();

        assertThat(responses).singleElement().satisfies(response -> {
            assertThat(response.title()).isEqualTo("Clean Code");
            assertThat(response.author()).isEqualTo("Robert C. Martin");
            assertThat(response.isbn()).isEqualTo("9780132350884");
        });
    }

    @Test
    void findByIdThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(7L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("No se encontro ningun libro con id=7");
    }

    @Test
    void createRejectsDuplicateIsbn() {
        BookRequest request = new BookRequest("Title", "Author", "1234567890", 2020);
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessage("Ya existe un libro registrado con ISBN=1234567890");
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @SuppressWarnings("null")
    void createSavesAndReturnsBook() {
        BookRequest request = new BookRequest("Title", "Author", "1234567890", 2020);
        Book savedBook = new Book(request.title(), request.author(), request.isbn(), request.publishedYear());
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse response = bookService.create(request);

        assertThat(response.title()).isEqualTo(request.title());
        ArgumentCaptor<Book> savedBookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(savedBookCaptor.capture());
        assertThat(savedBookCaptor.getValue().getTitle()).isEqualTo(request.title());
    }

    @Test
    void updateRejectsIsbnOwnedByAnotherBook() {
        Book existing = mock(Book.class);
        Book conflicting = mock(Book.class);
        when(conflicting.getId()).thenReturn(2L);
        BookRequest request = new BookRequest("Updated", "Author", "1234567890", 2021);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> bookService.update(1L, request))
                .isInstanceOf(DuplicateIsbnException.class);
    }

    @Test
    void updateChangesBookFieldsWhenIsbnIsAvailable() {
        Book book = mock(Book.class);
        when(book.getId()).thenReturn(1L);
        BookRequest request = new BookRequest("Updated", "Author", "1234567890", 2021);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());
        when(book.getTitle()).thenReturn(request.title());
        when(book.getAuthor()).thenReturn(request.author());
        when(book.getIsbn()).thenReturn(request.isbn());
        when(book.getPublishedYear()).thenReturn(request.publishedYear());

        BookResponse response = bookService.update(1L, request);

        verify(book).setTitle(request.title());
        verify(book).setAuthor(request.author());
        verify(book).setIsbn(request.isbn());
        verify(book).setPublishedYear(request.publishedYear());
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void deleteRejectsMissingBook() {
        when(bookRepository.existsById(4L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.delete(4L))
                .isInstanceOf(BookNotFoundException.class);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void deleteRemovesExistingBook() {
        when(bookRepository.existsById(4L)).thenReturn(true);

        bookService.delete(4L);

        verify(bookRepository).deleteById(4L);
    }
}
