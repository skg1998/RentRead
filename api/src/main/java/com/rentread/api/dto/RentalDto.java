package com.rentread.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDto {
	private Long rentalId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime rentedAt;
    private boolean active;
}
