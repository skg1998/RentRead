package com.rentread.api.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rentread.api.dto.AddBookRequest;
import com.rentread.api.dto.BookDto;
import com.rentread.api.dto.UpdateBookRequest;
import com.rentread.api.entity.Book;
import com.rentread.api.entity.User;
import com.rentread.api.enumeration.Role;
import com.rentread.api.exceptions.BadRequestException;
import com.rentread.api.repository.BookRepository;
import com.rentread.api.service.UserService;
import com.rentread.api.serviceImpls.BookServiceImpl;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook_AdminRole_Success() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);
        when(userService.getUser()).thenReturn(adminUser);

        AddBookRequest addBookRequest = new AddBookRequest();
        addBookRequest.setAuthor("Author");
        addBookRequest.setTitle("Title");
        addBookRequest.setGenre("Genre");

        bookService.addBook(addBookRequest);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBook_NonAdminRole_ThrowsException() {
        User user = new User();
        user.setRole(Role.USER);
        when(userService.getUser()).thenReturn(user);

        AddBookRequest addBookRequest = new AddBookRequest();

        assertThrows(BadRequestException.class, () -> bookService.addBook(addBookRequest));
    }

    @Test
    void testUpdateBook_AdminRole_Success() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);
        when(userService.getUser()).thenReturn(adminUser);

        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("Updated Title");
        updateBookRequest.setGenre("Updated Genre");

        bookService.updateBook(1L, updateBookRequest);

        verify(bookRepository, times(1)).save(book);
        assertEquals("Updated Title", book.getTitle());
        assertEquals("Updated Genre", book.getGenre());
    }

    @Test
    void testUpdateBook_NonAdminRole_ThrowsException() {
        User user = new User();
        user.setRole(Role.USER);
        when(userService.getUser()).thenReturn(user);

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();

        assertThrows(BadRequestException.class, () -> bookService.updateBook(1L, updateBookRequest));
    }

    @Test
    void testDeleteBook_AdminRole_Success() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);
        when(userService.getUser()).thenReturn(adminUser);

        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_NonAdminRole_ThrowsException() {
        User user = new User();
        user.setRole(Role.USER);
        when(userService.getUser()).thenReturn(user);

        assertThrows(BadRequestException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    void testGetBook_Success() {
        Book book = new Book();
        book.setTitle("Title");
        book.setGenre("Genre");
        book.setAvailabilityStatus(true);
        book.setAuthor("Author");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBook(1L);

        assertEquals("Title", bookDto.getTitle());
        assertEquals("Genre", bookDto.getGenre());
        assertTrue(bookDto.getAvailabilityStatus());
        assertEquals("Author", bookDto.getAuthor());
    }

    @Test
    void testGetBooks_Success() {
        Book book1 = new Book();
        book1.setTitle("Title1");
        book1.setGenre("Genre1");
        book1.setAvailabilityStatus(true);
        book1.setAuthor("Author1");

        Book book2 = new Book();
        book2.setTitle("Title2");
        book2.setGenre("Genre2");
        book2.setAvailabilityStatus(false);
        book2.setAuthor("Author2");

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDtos = bookService.getBooks();

        assertEquals(2, bookDtos.size());
        assertEquals("Title1", bookDtos.get(0).getTitle());
        assertEquals("Title2", bookDtos.get(1).getTitle());
    }

    @Test
    void testCheckAvailability_Available() {
        Book book = new Book();
        book.setAvailabilityStatus(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertTrue(bookService.checkAvailability(1L));
    }

    @Test
    void testCheckAvailability_NotAvailable() {
        Book book = new Book();
        book.setAvailabilityStatus(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertFalse(bookService.checkAvailability(1L));
    }
}
