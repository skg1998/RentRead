package com.rentread.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentread.api.entity.Rental;
import com.rentread.api.entity.User;

public interface RentalRepository extends JpaRepository<Rental, Long>{

	Optional<Rental> findByUserAndActive(User user, boolean b);

}
