package com.rentread.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "rental_tbl")
public class Rental {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rental_id") 
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
	
	@Column(nullable = false)
    private Boolean active = true;
	
	@Column(name = "rented_at", nullable = false)
    private LocalDateTime rentedAt = LocalDateTime.now();

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
	
	@PrePersist
    public void prePersist() {
        this.rentedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void preUpdate() {
        this.returnedAt = LocalDateTime.now();
    }
}
