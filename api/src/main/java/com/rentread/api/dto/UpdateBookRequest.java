package com.rentread.api.dto;

import com.rentread.api.enumeration.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {
	private String title;
	private String author;
	private String genre;
	private Boolean bookStatus;
}
