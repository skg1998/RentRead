package com.rentread.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {
	@NotNull(message = "Book title can not be null.")
	private String title;
	
	@NotNull(message = "Book author can not be null.")
	private String author;
	
	@NotNull(message = "Book genre can not be null.")
	private String genre;
	
	private Boolean bookStatus;
}
