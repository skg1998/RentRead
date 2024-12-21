package com.rentread.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentread.api.dto.AppResponse;
import com.rentread.api.dto.RentalDto;
import com.rentread.api.dto.SuccessResponse;
import com.rentread.api.service.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rent")
@RequiredArgsConstructor
public class RentalController {
	private final RentalService rentalService;
	
	@GetMapping("/book/{bookId}")
    public ResponseEntity<AppResponse> rentBook(@PathVariable("bookId") long bookId) {
		RentalDto rentalDto = rentalService.rentBook(bookId);
        return responseMaker("rent book successfully", HttpStatus.OK, rentalDto);
    }
	
	@GetMapping("/book/return/{bookId}")
    public ResponseEntity<AppResponse> returnBook(@PathVariable("bookId") long bookId) {
        rentalService.renturnBook(bookId);
        return responseMaker("return book successfully", HttpStatus.OK, null);
    }
    
    @SuppressWarnings("rawtypes")
	private <T> ResponseEntity<AppResponse> responseMaker(String message, HttpStatus httpStatus, T data){
		SuccessResponse successResponse = new SuccessResponse<>(message, data);
        AppResponse response = new AppResponse(true, successResponse, null);
        return response.toResponseEntity(httpStatus);
	}

}
