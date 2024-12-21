package com.rentread.api.serviceImpls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rentread.api.dto.AddBookRequest;
import com.rentread.api.dto.BookDto;
import com.rentread.api.dto.UpdateBookRequest;
import com.rentread.api.entity.Book;
import com.rentread.api.entity.User;
import com.rentread.api.exceptions.BadRequestException;
import com.rentread.api.exceptions.NotFoundException;
import com.rentread.api.repository.BookRepository;
import com.rentread.api.service.BookService;
import com.rentread.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
	private final BookRepository bookRepository;
	private final UserService userService;

	@Override
	public void addBook(AddBookRequest addBookRequest) {
		//validate user
		User user = userService.getUser();
		
		if(!user.getRole().name().equals("ADMIN")) {
			throw new BadRequestException("Only admin is allow to create a book");
		}
		
		Book book = new Book();
		book.setAuthor(addBookRequest.getAuthor());
		book.setAvailabilityStatus(true);
		book.setGenre(addBookRequest.getGenre());
		book.setTitle(addBookRequest.getTitle());
		
		bookRepository.save(book);
	}

	@Override
	public void updateBook(long bookId, UpdateBookRequest updateBookRequest) {
		//Validate bookId
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		Book book = optionalBook.orElseThrow(() -> new NotFoundException("Book with given id not found"));
		
		//Validate userId
		User user = userService.getUser();
		if(!user.getRole().name().equals("ADMIN")) {
			throw new BadRequestException("Only admin is allow to update a book");
		}
		
		book.setTitle(updateBookRequest.getTitle());
		book.setGenre(updateBookRequest.getGenre());
		
		bookRepository.save(book);
	}

	@Override
	public void deleteBook(long bookId) {
		//Validate userId
		User user = userService.getUser();
		if(!user.getRole().name().equals("ADMIN")) {
			throw new BadRequestException("Only admin is allow to delete a book");
		}
				
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		Book book = optionalBook.orElseThrow(() -> new NotFoundException("Book with given id not found"));
		
		bookRepository.delete(book);
	}

	@Override
	public BookDto getBook(long bookId) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		Book book = optionalBook.orElseThrow(() -> new NotFoundException("Book with given id not found"));
		
		BookDto bookDto = new BookDto();
		bookDto.setTitle(book.getTitle());
		bookDto.setGenre(book.getGenre());
		bookDto.setAvailabilityStatus(book.getAvailabilityStatus());
		bookDto.setAuthor(book.getAuthor());
		bookDto.setCreatedAt(book.getCreatedAt());
		bookDto.setUpdatedAt(book.getUpdatedAt());
		
		return bookDto;
	}

	@Override
	public List<BookDto> getBooks() {
		List<Book> books = bookRepository.findAll();
		
		List<BookDto> bookDtos = new ArrayList<>();
		books.forEach(book -> {
			BookDto bookDto = new BookDto();
			bookDto.setTitle(book.getTitle());
			bookDto.setGenre(book.getGenre());
			bookDto.setAvailabilityStatus(book.getAvailabilityStatus());
			bookDto.setAuthor(book.getAuthor());
			bookDto.setCreatedAt(book.getCreatedAt());
			bookDto.setUpdatedAt(book.getUpdatedAt());
			
			bookDtos.add(bookDto);
		});
		
		return bookDtos;
	}

	@Override
	public boolean checkAvailability(long bookId) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		Book book = optionalBook.orElseThrow(() -> new NotFoundException("Book with given id not found"));
		return book.getAvailabilityStatus();
	}

}
