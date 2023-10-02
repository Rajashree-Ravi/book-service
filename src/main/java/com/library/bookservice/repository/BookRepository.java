package com.library.bookservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.library.bookservice.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByPublisher(String publisher);

	List<Book> findByTitleContaining(String title);

	List<Book> findByAuthorId(long authorId);

	List<Book> findByBorrowerId(long borrowerId);

}