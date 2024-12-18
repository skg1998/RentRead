package com.rentread.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentread.api.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long>{

}
