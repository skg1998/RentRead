package com.rentread.api.serviceImpls;

import java.time.LocalDateTime;
import java.util.Optional;

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
public class RentalServiceImple implements RentalService{
	private final RentalRepository rentalRepository;
	private final BookRepository bookRepository;
	private final UserService userService;
	
	@Override
	public RentalDto rentBook(long bookId) {
		User user = userService.getUser();
//		// Check if the user exists
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the book exists and is available
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (Boolean.FALSE.equals(book.getAvailabilityStatus())) {
            throw new IllegalStateException("Book is currently unavailable");
        }
        
        // Check if the user already has an active rental
        Optional<Rental> activeRental = rentalRepository.findByUserAndActive(user, true);
        if (activeRental.isPresent()) {
            throw new IllegalStateException("User already has an active rental");
        }

        // Create the rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentedAt(LocalDateTime.now());
        rental.setActive(true);

        // Mark the book as unavailable
        book.setAvailabilityStatus(false);
        bookRepository.save(book);
        
        // Save the rental
        Rental savedRental = rentalRepository.save(rental);

     // Return RentalDto
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
		Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
		Rental rental = optionalRental.orElseThrow(() -> new IllegalArgumentException("Rental not found for given id"));
		
		if (Boolean.FALSE.equals(rental.getActive())) {
            throw new IllegalArgumentException("Rental is already completed");
        }
		
		// Mark the rental as inactive
        rental.setActive(false);
        rental.setReturnedAt(LocalDateTime.now());
        
        // Mark the book as available
        Book book = rental.getBook();
        book.setAvailabilityStatus(true);
        bookRepository.save(book);
		
		rentalRepository.save(rental);
	}
	
}
