package com.rentread.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentread.api.dto.AddBookRequest;
import com.rentread.api.dto.AppResponse;
import com.rentread.api.dto.BookDto;
import com.rentread.api.dto.SuccessResponse;
import com.rentread.api.dto.UpdateBookRequest;
import com.rentread.api.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;
	
	@PostMapping("/")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse> addBook(@RequestBody AddBookRequest addBookRequest) {
		bookService.addBook(addBookRequest);
        return responseMaker("add book successfully", HttpStatus.OK, null);
    }
	
	@PutMapping("/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse> updateBook(@PathVariable("bookId") long bookId, @RequestBody UpdateBookRequest updateBookRequest) {
		bookService.updateBook(bookId, updateBookRequest);
        return responseMaker("update book successfully", HttpStatus.OK, null);
    }
	
	@DeleteMapping("/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse> deleteBook(@PathVariable("bookId") long bookId) {
		bookService.deleteBook(bookId);
        return responseMaker("delete book successfully", HttpStatus.OK, null);
    }
	
	@GetMapping("/{bookId}")
    public ResponseEntity<AppResponse> getBook(@PathVariable("bookId") long bookId) {
		BookDto bookDto = bookService.getBook(bookId);
        return responseMaker("fetch book successfully", HttpStatus.OK, bookDto);
    }
	
	@GetMapping("/")
    public ResponseEntity<AppResponse> getBooks() {
		List<BookDto> bookDtos = bookService.getBooks();
        return responseMaker("fetch all book successfully", HttpStatus.OK, bookDtos);
    }
	
	@GetMapping("/avaliable/{bookId}")
    public ResponseEntity<AppResponse> checkAvailibility(@PathVariable("bookId") long bookId) {
		Boolean isAvailable = bookService.checkAvailability(bookId);
        return responseMaker("check book avability successfully", HttpStatus.OK, isAvailable);
    }
    
    @SuppressWarnings("rawtypes")
	private <T> ResponseEntity<AppResponse> responseMaker(String message, HttpStatus httpStatus, T data){
		SuccessResponse successResponse = new SuccessResponse<>(message, data);
        AppResponse response = new AppResponse(true, successResponse, null);
        return response.toResponseEntity(httpStatus);
	}

}
