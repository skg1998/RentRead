package com.rentread.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentread.api.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
