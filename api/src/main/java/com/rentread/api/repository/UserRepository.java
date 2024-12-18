package com.rentread.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentread.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
