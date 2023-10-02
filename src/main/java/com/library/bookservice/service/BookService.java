package com.library.bookservice.service;

import java.util.List;

import com.library.bookservice.entity.Book;

public interface BookService {

	List<Book> getAllBooks(String title);

	Book getBookById(long id);

	Book createBook(Book book);

	Book updateBook(long id, Book book);

	void deleteBook(long id);

	List<Book> findByAuthor(long authorId);

	List<Book> findByBorrower(long borrowerId);
	
	List<Book> removeAuthorId(long authorId);
	
	List<Book> removeBorrowerId(long borrowerId);
}
