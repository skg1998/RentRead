package com.rentread.api.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rentread.api.dto.RentalDto;
import com.rentread.api.entity.Book;
import com.rentread.api.entity.Rental;
import com.rentread.api.entity.User;
import com.rentread.api.repository.BookRepository;
import com.rentread.api.repository.RentalRepository;
import com.rentread.api.service.UserService;
import com.rentread.api.serviceImpls.RentalServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

public class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private User user;
    private Book book;
    private Rental rental;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAvailabilityStatus(true);

        rental = new Rental();
        rental.setId(1L);
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentedAt(LocalDateTime.now());
        rental.setActive(true);
    }

    @Test
    void rentBook_Success() {
        when(userService.getUser()).thenReturn(user);
        when(rentalRepository.countByUserAndActive(user, true)).thenReturn(1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        RentalDto rentalDto = rentalService.rentBook(1L);

        assertNotNull(rentalDto);
        assertEquals(rental.getId(), rentalDto.getRentalId());
        assertEquals(book.getId(), rentalDto.getBookId());
        assertFalse(book.getAvailabilityStatus());
        verify(bookRepository, times(1)).save(book);
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    void rentBook_ExceedsActiveRentalsLimit() {
        when(userService.getUser()).thenReturn(user);
        when(rentalRepository.countByUserAndActive(user, true)).thenReturn(2);

        Exception exception = assertThrows(IllegalStateException.class, () -> rentalService.rentBook(1L));

        assertEquals("User cannot have more than two active rentals", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void rentBook_BookUnavailable() {
        book.setAvailabilityStatus(false);
        when(userService.getUser()).thenReturn(user);
        when(rentalRepository.countByUserAndActive(user, true)).thenReturn(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(IllegalStateException.class, () -> rentalService.rentBook(1L));

        assertEquals("Book is currently unavailable", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void rentBook_BookNotFound() {
        when(userService.getUser()).thenReturn(user);
        when(rentalRepository.countByUserAndActive(user, true)).thenReturn(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> rentalService.rentBook(1L));

        assertEquals("Book not found", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void returnBook_Success() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.renturnBook(1L);

        assertFalse(rental.getActive());
        assertNotNull(rental.getReturnedAt());
        assertTrue(book.getAvailabilityStatus());
        verify(bookRepository, times(1)).save(book);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void returnBook_AlreadyCompleted() {
        rental.setActive(false);
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        Exception exception = assertThrows(IllegalStateException.class, () -> rentalService.renturnBook(1L));

        assertEquals("Rental is already completed", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void returnBook_RentalNotFound() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> rentalService.renturnBook(1L));

        assertEquals("Rental not found for the given ID", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
        verify(rentalRepository, never()).save(any(Rental.class));
    }
}
