package com.rentread.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddBookRequest {
	@NotEmpty(message = "Title is required.")
    private String title;

    @NotEmpty(message = "Author is required.")
    private String author;

    @NotEmpty(message = "Genre is required.")
    private String genre;

    private Boolean bookStatus;

    // Custom getter to provide a default value for bookStatus
    public Boolean getBookStatus() {
        return bookStatus == null ? true : bookStatus;
    }
}
