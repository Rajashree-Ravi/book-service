package com.library.bookservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.bookservice.entity.Book;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> getAllBooks(String title) {
		List<Book> books = new ArrayList<Book>();

		if (title == null)
			bookRepository.findAll().forEach(books::add);
		else
			bookRepository.findByTitleContaining(title).forEach(books::add);

		return books;
	}

	@Override
	public Book getBookById(long id) {
		Optional<Book> book = bookRepository.findById(id);
		return (book.isPresent() ? book.get() : null);
	}

	@Override
	public Book createBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book updateBook(long id, Book book) {

		Optional<Book> updatedBook = bookRepository.findById(id).map(existingBook -> {
			return bookRepository.save(existingBook.updateWith(book));
		});

		return (updatedBook.isPresent() ? updatedBook.get() : null);
	}

	@Override
	public void deleteBook(long id) {
		bookRepository.deleteById(id);
	}

	@Override
	public List<Book> findByAuthor(long authorId) {
		return bookRepository.findByAuthorId(authorId);
	}

	@Override
	public List<Book> findByBorrower(long borrowerId) {
		return bookRepository.findByBorrowerId(borrowerId);
	}

}
