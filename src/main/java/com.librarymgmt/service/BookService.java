package com.librarymgmt.service;

import com.librarymgmt.model.Book;
import com.librarymgmt.util.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    public Book addBook(Book book) {
        try {
            DatabaseHelper.saveBook(book);
            System.out.println("Book saved: " + book.getIsbn());
        } catch (SQLException e) {
            System.err.println("Error saving book: " + e.getMessage());
        }
        return book;
    }

    public List<Book> listAllBooks() {
        try {
            return DatabaseHelper.fetchAllBooks();
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
            return List.of();
        }
    }

    public Book findByIsbn(String isbn) {
        try {
            return DatabaseHelper.fetchBookByIsbn(isbn);
        } catch (SQLException e) {
            System.err.println("Error fetching book: " + e.getMessage());
            return null;
        }
    }

    public List<Book> getAllBooks() {
        return List.of();
    }
}


