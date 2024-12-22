package com.rentread.api.serviceImpls;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.rentread.api.dto.RentalDto;
import com.rentread.api.entity.Book;
import com.rentread.api.entity.Rental;
import com.rentread.api.entity.User;
import com.rentread.api.repository.BookRepository;
import com.rentread.api.repository.RentalRepository;
import com.rentread.api.service.RentalService;
import com.rentread.api.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    @Transactional
    public RentalDto rentBook(long bookId) {
        // Get the current authenticated user
        User user = userService.getUser();

        // Check if the user has less than two active rentals
        int activeRentalCount = rentalRepository.countByUserAndActive(user, true);
        if (activeRentalCount >= 2) {
            throw new IllegalStateException("User cannot have more than two active rentals");
        }

        // Check if the book exists and is available
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (Boolean.FALSE.equals(book.getAvailabilityStatus())) {
            throw new IllegalStateException("Book is currently unavailable");
        }

        // Create and save the rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentedAt(LocalDateTime.now());
        rental.setActive(true);

        // Update the book's availability status
        book.setAvailabilityStatus(false);
        bookRepository.save(book);

        // Save the rental
        Rental savedRental = rentalRepository.save(rental);

        // Build and return the RentalDto
        return RentalDto.builder()
                .rentalId(savedRental.getId())
                .userId(user.getId())
                .userName(user.getFirstName() + " " + user.getLastName())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .rentedAt(savedRental.getRentedAt())
                .active(savedRental.getActive())
                .build();
    }

    @Override
    @Transactional
    public void renturnBook(long rentalId) {
        // Fetch the rental by ID
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found for the given ID"));

        // Check if the rental is already completed
        if (Boolean.FALSE.equals(rental.getActive())) {
            throw new IllegalStateException("Rental is already completed");
        }

        // Mark the rental as inactive
        rental.setActive(false);
        rental.setReturnedAt(LocalDateTime.now());

        // Update the book's availability status
        Book book = rental.getBook();
        book.setAvailabilityStatus(true);
        bookRepository.save(book);

        // Save the updated rental
        rentalRepository.save(rental);
    }
}
