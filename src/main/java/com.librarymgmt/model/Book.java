package com.librarymgmt.model;

public class Book {

    private String isbn;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private String bookId;

    public Book(String isbn, String title, String author, String category, int totalCopies, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.bookId = bookId;
    }

    public Book() {}

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    @Override
    public String toString() {
        return String.format("%s — %s by %s (cat: %s) copies: %d available: %d",
                isbn, title, author, category, totalCopies, availableCopies, bookId);
    }
}


