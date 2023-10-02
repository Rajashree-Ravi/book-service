package com.library.bookservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Class representing a book in the library application.")
@Entity
@Data
@NoArgsConstructor
@Table(name = "book")
@JsonPropertyOrder({ "id", "title", "description", "publisher", "ISBN", "status", "authorId", "borrowerId" })
public class Book {

	@ApiModel(description = "Class representing book status")
	private enum BookStatus {
		AVAILABLE, RESERVED, BORROWED, LOST;
	}

	@ApiModelProperty(notes = "Unique identifier of the Book.", example = "1")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Name of the Book.", example = "Alchemist", required = true)
	@NotBlank
	private String title;

	@ApiModelProperty(notes = "Short Description of the book.", example = "A magical fable about following your dreams")
	@NotBlank
	private String description;

	@ApiModelProperty(notes = "Publisher of the book.", example = "HarperCollins Publishers")
	@NotBlank
	private String publisher;

	@ApiModelProperty(notes = "13-digit International Standard Book Number.", example = "B00746254132X")
	@NotBlank
	private String ISBN;

	@ApiModelProperty(notes = "ID of the author.", example = "3")
	private Long authorId;

	@ApiModelProperty(notes = "ID of the borrower.", example = "7")
	private Long borrowerId;

	@ApiModelProperty(notes = "Status of the book.", example = "AVAILABLE", required = true)
	@Enumerated(EnumType.STRING)
	private BookStatus status;

	public Book(Long id, String title, String description, String publisher, String ISBN, Long authorId,
			Long borrowerId, BookStatus status) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.publisher = publisher;
		this.ISBN = ISBN;
		this.authorId = authorId;
		this.borrowerId = borrowerId;
		this.status = status;
	}

	public Book updateWith(Book book) {
		return new Book(this.id, book.title, book.description, book.publisher, book.ISBN, book.authorId,
				book.borrowerId, book.status);
	}

}
