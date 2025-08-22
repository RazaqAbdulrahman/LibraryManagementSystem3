package com.librarymgmt.model;

import java.time.LocalDate;

public class Loan {
    private String loanId;
    private String bookId;
    private String memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String loanId, String bookId, String memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Loan() {}

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return String.format("Loan[%s] book=%s member=%s loan=%s due=%s return=%s",
                loanId, bookId, memberId,
                loanDate == null ? "-" : loanDate.toString(),
                dueDate == null ? "-" : dueDate.toString(),
                returnDate == null ? "-" : returnDate.toString());
    }
}


