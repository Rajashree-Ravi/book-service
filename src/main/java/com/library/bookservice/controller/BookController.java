package com.library.bookservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.common.exception.LibraryException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.library.bookservice.entity.Book;
import com.library.bookservice.service.BookService;

@RestController
@Api(produces = "application/json", value = "Operations pertaining to manage books in the library application")
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	@ApiOperation(value = "View all books", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved all books"),
			@ApiResponse(code = 204, message = "Books list is empty"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	private ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title) {
		List<Book> books = bookService.getAllBooks(title);

		if (books.isEmpty())
			throw new LibraryException("no-content", "Books list is empty", HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Retrieve specific book with the specified book id", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved book with the book id"),
			@ApiResponse(code = 404, message = "Book with specified book id not found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	private ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
		Book book = bookService.getBookById(id);

		if (book != null)
			return new ResponseEntity<>(book, HttpStatus.OK);
		else
			throw new LibraryException("book-not-found", String.format("Book with id=%d not found", id),
					HttpStatus.NOT_FOUND);
	}

	@PostMapping
	@ApiOperation(value = "Create a new book", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created a book"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		try {
			return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Update a book information", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated book information"),
			@ApiResponse(code = 404, message = "Book with specified book id not found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
		Book updatedBook = bookService.updateBook(id, book);

		if (updatedBook != null)
			return new ResponseEntity<>(updatedBook, HttpStatus.OK);
		else
			throw new LibraryException("book-not-found", String.format("Book with id=%d not found", id),
					HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete a book", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Successfully deleted book information"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	private ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
		try {
			bookService.deleteBook(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/author/{id}")
	@ApiOperation(value = "Retrieve all books with the specified author id", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved books with the author id"),
			@ApiResponse(code = 404, message = "No Books found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	private ResponseEntity<List<Book>> findByAuthor(@PathVariable("id") long authorId) {
		List<Book> books = bookService.findByAuthor(authorId);

		if (books.isEmpty())
			throw new LibraryException("no-content", "Books list is empty", HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/borrower/{id}")
	@ApiOperation(value = "Retrieve all books with the specified borrower id", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved book with the borrower id"),
			@ApiResponse(code = 404, message = "No Books found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	private ResponseEntity<List<Book>> findByBorrower(@PathVariable("id") long borrowerId) {
		List<Book> books = bookService.findByBorrower(borrowerId);

		if (books.isEmpty())
			throw new LibraryException("no-content", "Books list is empty", HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(books, HttpStatus.OK);
	}
	
	@PutMapping("/author/{id}")
	@ApiOperation(value = "Update books by author", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the books"),
			@ApiResponse(code = 404, message = "Books with specified author id not found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	public ResponseEntity<String> removeAuthor(@PathVariable("id") long id) {
		List<Book> updatedBooks = bookService.removeAuthorId(id);

		if (updatedBooks.isEmpty())
			throw new LibraryException("books-not-found", String.format("Books with the author id=%d not found", id),
					HttpStatus.NOT_FOUND);

		return new ResponseEntity<>("Books updated successfully", HttpStatus.OK);
	}
	
	@PutMapping("/borrower/{id}")
	@ApiOperation(value = "Update books by borrower", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the books"),
			@ApiResponse(code = 404, message = "Books with specified borrower id not found"),
			@ApiResponse(code = 500, message = "Application failed to process the request") })
	public ResponseEntity<String> removeBorrower(@PathVariable("id") long id) {
		List<Book> updatedBooks = bookService.removeBorrowerId(id);

		if (updatedBooks.isEmpty())
			throw new LibraryException("books-not-found", String.format("Books with the borrower id=%d not found", id),
					HttpStatus.NOT_FOUND);

		return new ResponseEntity<>("Books updated successfully", HttpStatus.OK);
	}
}
