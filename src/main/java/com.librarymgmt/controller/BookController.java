package com.librarymgmt.controller;

import com.librarymgmt.model.Book;
import com.librarymgmt.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")  // all book requests go here
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Add a book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
}

