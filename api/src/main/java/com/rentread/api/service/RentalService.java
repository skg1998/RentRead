package com.rentread.api.service;

import com.rentread.api.dto.BookDto;
import com.rentread.api.dto.RentalDto;

public interface RentalService {
	public RentalDto rentBook(long bookId);
	public void renturnBook(long bookId);
	
}
