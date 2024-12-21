package com.rentread.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id") 
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String author;
	
	@Column(nullable = false)
	private String genre;
	
	@Column(name = "availability_status", nullable = false)
	private Boolean availabilityStatus = true;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
