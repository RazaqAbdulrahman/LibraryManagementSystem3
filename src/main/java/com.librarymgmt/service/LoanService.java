package com.librarymgmt.service;

import com.librarymgmt.model.Loan;
import com.librarymgmt.util.DatabaseHelper;
import com.librarymgmt.util.IdGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    public boolean loanBook(String bookIsbn, String memberId, int loanDays) {
        Loan loan = new Loan();
        loan.setLoanId(IdGenerator.generate("LN"));
        loan.setBookId(bookIsbn);
        loan.setMemberId(memberId);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(Math.max(7, loanDays)));
        loan.setReturnDate(null);

        try {
            boolean ok = DatabaseHelper.saveLoan(loan);
            if (!ok) {
                System.out.println("Could not create loan: either book not found or no copies available.");
                return false;
            }
            System.out.println("Loan created: " + loan);
            return true;
        } catch (SQLException ex) {
            System.err.println("Loan creation error: " + ex.getMessage());
            return false;
        }
    }

    public boolean recordReturn(String loanId) {
        try {
            boolean ok = DatabaseHelper.recordReturn(loanId);
            if (!ok) {
                System.out.println("Could not record return (loan not found or already returned).");
                return false;
            }
            System.out.println("Return recorded for loan: " + loanId);
            return true;
        } catch (SQLException ex) {
            System.err.println("Return error: " + ex.getMessage());
            return false;
        }
    }

    public List<Loan> listAllLoans() {
        try {
            return DatabaseHelper.fetchAllLoans();
        } catch (SQLException ex) {
            System.err.println("Fetch loans error: " + ex.getMessage());
            return List.of();
        }
    }

    public boolean borrowBook(String isbn, int memberId, int days) {
        return false;
    }

    public boolean returnBook(String isbn, int memberId) {
        return false;
    }

    public Loan findByLoanId(String id) {
        return null;
    }
}

