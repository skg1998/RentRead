package com.rentread.api.service;

import com.rentread.api.dto.RentalDto;

public interface RentalService {
	public RentalDto rentBook(long rentalId);
	public void renturnBook(long rentalId);
	
}
