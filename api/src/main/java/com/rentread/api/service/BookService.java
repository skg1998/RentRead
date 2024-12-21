package com.rentread.api.service;

import java.util.List;

import com.rentread.api.dto.AddBookRequest;
import com.rentread.api.dto.BookDto;
import com.rentread.api.dto.UpdateBookRequest;

public interface BookService {
	public void addBook(AddBookRequest addBookRequest);
	public void updateBook(long bookId, UpdateBookRequest updateBookRequest);
	public void deleteBook(long bookId);
	public BookDto getBook(long bookId);
	public List<BookDto> getBooks();
	public boolean checkAvailability(long bookId); 
	
}
