package com.librarymgmt.controller;

import com.librarymgmt.model.Book;
import com.librarymgmt.model.Loan;
import com.librarymgmt.service.BookService;
import com.librarymgmt.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final BookService bookService;
    private final LoanService loanService;

    public ReportController(BookService bookService, LoanService loanService) {
        this.bookService = bookService;
        this.loanService = loanService;
    }

    @GetMapping("/books/summary")
    public ResponseEntity<?> booksSummary() {
        List<Book> books = bookService.listAllBooks();
        int totalBooks = books.size();
        int totalCopies = books.stream().mapToInt(Book::getTotalCopies).sum();
        int availableCopies = books.stream().mapToInt(Book::getAvailableCopies).sum();
        return ResponseEntity.ok(
                java.util.Map.of(
                        "totalUniqueBooks", totalBooks,
                        "totalCopies", totalCopies,
                        "availableCopies", availableCopies
                )
        );
    }

    @GetMapping("/loans/active")
    public ResponseEntity<List<Loan>> activeLoans() {
        List<Loan> loans = loanService.listAllLoans();
        List<Loan> active = loans.stream().filter(l -> l.getReturnDate() == null).toList();
        return ResponseEntity.ok(active);
    }
}
