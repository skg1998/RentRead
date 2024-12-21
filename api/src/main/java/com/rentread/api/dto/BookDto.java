package com.rentread.api.dto;

import java.time.LocalDateTime;

import com.rentread.api.enumeration.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
	private String title;
	private String author;
	private String genre;
	private Boolean availabilityStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
