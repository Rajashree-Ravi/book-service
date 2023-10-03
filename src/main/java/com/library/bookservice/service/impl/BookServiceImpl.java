package com.library.bookservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.library.bookservice.entity.Book;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.service.BookService;
import com.library.common.exception.LibraryException;

@Service
public class BookServiceImpl implements BookService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;
	
	private final RestTemplate restTemplate;
	private static final String AUTHOR_SERVICE_URL = "http://localhost:8081/api/authors";
	private static final String BORROWER_SERVICE_URL = "http://localhost:8082/api/borrowers";

	public BookServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

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
		boolean authorExists = true;
		boolean borrowerExists = true;
		
		if (book.getAuthorId() != null && book.getAuthorId() != 0)
			authorExists = checkIfAuthorExists(book.getAuthorId());
		
		if (book.getBorrowerId() != null && book.getBorrowerId() != 0)
			borrowerExists = checkIfBorrowerExists(book.getBorrowerId());
		
		if (authorExists && borrowerExists)
			return bookRepository.save(book);
		else if (!authorExists)
			throw new LibraryException("author-not-found", String.format("Author with id=%d not found", book.getAuthorId()) , HttpStatus.NOT_FOUND);
		else if (!borrowerExists)
			throw new LibraryException("borrower-not-found", String.format("Borrower with id=%d not found", book.getBorrowerId()) , HttpStatus.NOT_FOUND);
		
		return null;
	}

	@Override
	public Book updateBook(long id, Book book) {
		
		boolean authorExists = true;
		boolean borrowerExists = true;
		
		if (book.getAuthorId() != null && book.getAuthorId() != 0)
			authorExists = checkIfAuthorExists(book.getAuthorId());
		
		if (book.getBorrowerId() != null && book.getBorrowerId() != 0)
			borrowerExists = checkIfBorrowerExists(book.getBorrowerId());
		
		if (authorExists && borrowerExists) {
			Optional<Book> updatedBook = bookRepository.findById(id).map(existingBook -> {
				return bookRepository.save(existingBook.updateWith(book));
			});

			return (updatedBook.isPresent() ? updatedBook.get() : null);
		}
		else if (!authorExists)
			throw new LibraryException("author-not-found", String.format("Author with id=%d not found", book.getAuthorId()) , HttpStatus.NOT_FOUND);
		else if (!borrowerExists)
			throw new LibraryException("borrower-not-found", String.format("Borrower with id=%d not found", book.getBorrowerId()) , HttpStatus.NOT_FOUND);
		
		return null;
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

	@Override
	public List<Book> removeAuthorId(long authorId) {
		List<Book> updatedBooks = new ArrayList<>();
		bookRepository.findByAuthorId(authorId).forEach(book -> {
			book.setAuthorId(null);
			updatedBooks.add(bookRepository.save(book));
		});

		return updatedBooks;
	}

	@Override
	public List<Book> removeBorrowerId(long borrowerId) {
		List<Book> updatedBooks = new ArrayList<>();
		bookRepository.findByBorrowerId(borrowerId).forEach(book -> {
			book.setBorrowerId(null);
			updatedBooks.add(bookRepository.save(book));
		});

		return updatedBooks;
	}
	
	private boolean checkIfAuthorExists(long authorId) {
		ResponseEntity<Object> result = restTemplate.exchange(AUTHOR_SERVICE_URL + "/" + authorId, HttpMethod.GET, null, Object.class);
		LOGGER.info("Response Status: " + result.getStatusCode());
		LOGGER.info("Response Body: " + result.getBody());
		
		return (result.getStatusCode() == HttpStatus.OK) ? true : false;
	}
	
	private boolean checkIfBorrowerExists(long borrowerId) {
		ResponseEntity<Object> result = restTemplate.exchange(BORROWER_SERVICE_URL + "/" + borrowerId, HttpMethod.GET, null, Object.class);
		LOGGER.info("Response Status: " + result.getStatusCode());
		LOGGER.info("Response Body: " + result.getBody());

		return (result.getStatusCode() == HttpStatus.OK) ? true : false;
	}

}
