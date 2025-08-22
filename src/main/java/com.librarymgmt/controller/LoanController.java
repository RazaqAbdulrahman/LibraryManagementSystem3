package com.librarymgmt.controller;

import com.librarymgmt.model.Loan;
import com.librarymgmt.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // GET /api/loans
    @GetMapping
    public ResponseEntity<List<Loan>> getAll() {
        return ResponseEntity.ok(loanService.listAllLoans());
    }

    // POST /api/loans/borrow
    @PostMapping("/borrow")
    public ResponseEntity<?> borrow(@RequestParam String isbn,
                                    @RequestParam int memberId,
                                    @RequestParam(required = false, defaultValue = "14") int days) {
        boolean ok = loanService.borrowBook(isbn, memberId, days);
        if (!ok) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not borrow book (not found/no copies/member)");
        return ResponseEntity.status(HttpStatus.CREATED).body("Loan created");
    }

    // POST /api/loans/return
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam String isbn,
                                        @RequestParam int memberId) {
        boolean ok = loanService.returnBook(isbn, memberId);
        if (!ok) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not return book (no active loan found)");
        return ResponseEntity.ok("Book returned");
    }

    // GET /api/loans/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable String id) {
        Loan loan = loanService.findByLoanId(id);
        if (loan == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(loan);
    }
}

