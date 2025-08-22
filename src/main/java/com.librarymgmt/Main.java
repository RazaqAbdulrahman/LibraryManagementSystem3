/*
package com.librarymgmt;

import com.librarymgmt.model.*;
import com.librarymgmt.service.*;
import com.librarymgmt.util.DatabaseHelper;
import com.librarymgmt.util.IdGenerator;
import com.librarymgmt.validation.InputValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();
    private static final MemberService memberService = new MemberService();
    private static final LoanService loanService = new LoanService();
    private static final FineService fineService = new FineService();
    private static final StaffService staffService = new StaffService();
    private static final CategoryService categoryService = new CategoryService();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("     LIBRARY MANAGEMENT SYSTEM v1.0");
        System.out.println("==============================================");
        // Initialize database
        DatabaseHelper.initializeDatabase();
        // Main application loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageMembers();
                    break;
                case 3:
                    manageLoans();
                    break;
                case 4:
                    manageFines();
                    break;
                case 5:
                    manageStaff();
                    break;
                case 6:
                    manageCategories();
                    break;
                case 7:
                    generateReports();
                    break;
                case 0:
                    System.out.println("Thank you for using Library Management System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Members");
        System.out.println("3. Manage Loans");
        System.out.println("4. Manage Fines");
        System.out.println("5. Manage Staff");
        System.out.println("6. Manage Categories");
        System.out.println("7. Generate Reports");
        System.out.println("0. Exit");
        System.out.println("==================");
    }

    private static void manageBooks() {
        while (true) {
            System.out.println("\n=== BOOK MANAGEMENT ===");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book by ISBN");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    addNewBook();
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    searchBookByIsbn();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addNewBook() {
        System.out.println("\n--- Add New Book ---");

        String isbn = getStringInput("Enter ISBN: ");
        if (!InputValidator.isNonEmpty(isbn)) {
            System.out.println("ISBN cannot be empty!");
            return;
        }

        String title = getStringInput("Enter Title: ");
        if (!InputValidator.isNonEmpty(title)) {
            System.out.println("Title cannot be empty!");
            return;
        }

        String author = getStringInput("Enter Author: ");
        if (!InputValidator.isNonEmpty(author)) {
            System.out.println("Author cannot be empty!");
            return;
        }
        String category = getStringInput("Enter Category: ");
        int totalCopies = getIntInput("Enter Total Copies: ");
        if (totalCopies <= 0) {
            System.out.println("Total copies must be greater than 0!");
            return;
        }

        Book book = new Book(isbn, title, author, category, totalCopies, totalCopies);
        bookService.addBook(book);
    }

    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookService.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }

        System.out.printf("%-15s %-30s %-20s %-15s %-8s %-8s%n",
                "ISBN", "Title", "Author", "Category", "Total", "Available");
        System.out.println("=".repeat(100));
        for (Book book : books) {
            System.out.printf("%-15s %-30s %-20s %-15s %-8d %-8d%n",
                    book.getIsbn(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getCategory(), 15),
                    book.getTotalCopies(),
                    book.getAvailableCopies());
        }
    }

    private static void searchBookByIsbn() {
        System.out.println("\n--- Search Book by ISBN ---");
        String isbn = getStringInput("Enter ISBN: ");
        Book book = bookService.findByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found with ISBN: " + isbn);
        } else {
            System.out.println("Book found:");
            System.out.println(book);
        }
    }

    private static void manageMembers() {
        while (true) {
            System.out.println("\n=== MEMBER MANAGEMENT ===");
            System.out.println("1. Add New Member");
            System.out.println("2. View All Members");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addNewMember();
                    break;
                case 2:
                    viewAllMembers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addNewMember() {
        System.out.println("\n--- Add New Member ---");
        String name = getStringInput("Enter Name: ");
        if (!InputValidator.isNonEmpty(name)) {
            System.out.println("Name cannot be empty!");
            return;
        }

        String email = getStringInput("Enter Email: ");
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        String phone = getStringInput("Enter Phone: ");
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("Invalid phone format!");
            return;
        }

        memberService.createMember(name, email, phone);
    }

    private static void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        List<Member> members = memberService.listAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.printf("%-15s %-25s %-30s %-15s%n",
                "Member ID","Name","Email","Phone");
        System.out.println("=".repeat(85));
        for(Member member : members){
            System.out.printf("%-15s %-25s %-30s %-15s%n",
                    member.getMemberId(),
                    truncate(member.getName(),25),
                    truncate(member.getEmail(),30),
                    member.getPhone());
        }
    }

    private static void manageLoans(){
        while(true){
            System.out.println("\n=== LOAN MANAGEMENT ===");
            System.out.println("1. Issue Book Loan");
            System.out.println("2. Return Book");
            System.out.println("3. View All Loans");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice){

                case 1:
                issueBookLoan();
                break;
                case 2:
                returnBook();
                break;
                case 3:
                viewAllLoans();
                break;
                case 0:
                return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void issueBookLoan(){
        System.out.println("\n--- Issue Book Loan ---");
        String isbn = getStringInput("Enter Book ISBN: ");
        String memberId = getStringInput("Enter Member ID: ");
        int loanDays  =getIntInput("Enter Loan Duration (days, minimum 7): ");

        if(loanDays <7){
            System.out.println("Minimum loan duration is 7 days!");
            return;
        }

        boolean success = loanService.loanBook(isbn, memberId, loanDays);
        if(success){
            System.out.println("Book loan issued successfully!");
        }
    }

    private static void returnBook(){
        System.out.println("\n--- Return Book ---");
        String loanId = getStringInput("Enter Loan ID: ");
        boolean success = loanService.recordReturn(loanId);
        if(success){
            System.out.println("Book return recorded successfully!");
        }
    }

    private static void viewAllLoans() {
        System.out.println("\n--- All Loans ---");
        List<Loan> loans = loanService.listAllLoans();
        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        System.out.printf("%-15s %-15s %-15s %-12s %-12s %-12s%n",
                "Loan ID", "Book ID", "Member ID", "Loan Date", "Due Date", "Return Date");
        System.out.println("=".repeat(90));
        for (Loan loan : loans) {
            System.out.printf("%-15s %-15s %-15s %-12s %-12s %-12s%n",
                    loan.getLoanId(),
                    loan.getBookId(),
                    loan.getMemberId(),
                    loan.getLoanDate() != null ? loan.getLoanDate().toString() : "-",
                    loan.getDueDate() != null ? loan.getDueDate().toString() : "-",
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Not Returned");
        }
    }

    private static void manageFines() {
        while (true) {
            System.out.println("\n=== FINE MANAGEMENT ===");
            System.out.println("1. Issue Fine");
            System.out.println("2. View All Fines");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    issueFine();
                    break;
                case 2:
                    fineService.viewFines();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void issueFine() {
        System.out.println("\n--- Issue Fine ---");
        String memberId = getStringInput("Enter Member ID: ");
        double amount = getDoubleInput("Enter Fine Amount: ");
        String reason = getStringInput("Enter Reason: ");
        if (amount <= 0) {
            System.out.println("Fine amount must be greater than 0!");
            return;
        }
        String fineId = IdGenerator.generate("FN");
        Fine fine = new Fine(fineId, memberId, amount, reason, LocalDate.now(), false);
        fineService.issueFine(fine);
    }

    private static void manageStaff() {
        while (true) {
            System.out.println("\n=== STAFF MANAGEMENT ===");
            System.out.println("1. Add Staff");
            System.out.println("2. View All Staff");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    addStaff();
                    break;
                case 2:
                    staffService.viewStaff();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addStaff() {
        System.out.println("\n--- Add Staff ---");
        String name = getStringInput("Enter Name: ");
        String role = getStringInput("Enter Role: ");
        String email = getStringInput("Enter Email: ");
        String phone = getStringInput("Enter Phone: ");
        if (!InputValidator.isNonEmpty(name) || !InputValidator.isNonEmpty(role)) {
            System.out.println("Name and role cannot be empty!");
            return;
        }
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("Invalid phone format!");
            return;
        }
        String staffId = IdGenerator.generate("ST");

        Staff staff = new Staff(staffId, name, role, email, phone);
        staffService.addStaff(staff);
    }

    private static void manageCategories() {
        while (true) {
            System.out.println("\n=== CATEGORY MANAGEMENT ===");
            System.out.println("1. Add Category");
            System.out.println("2. View All Categories");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    categoryService.viewCategories();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCategory() {
        System.out.println("\n--- Add Category ---");
        String name = getStringInput("Enter Category Name: ");
        String description = getStringInput("Enter Description: ");
        if (!InputValidator.isNonEmpty(name)) {
            System.out.println("Category name cannot be empty!");
            return;
        }
        String categoryId = IdGenerator.generate("CAT");
        Category category = new Category(categoryId, name, description);
        categoryService.addCategory(category);
    }

    private static void generateReports() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Books Summary");
            System.out.println("2. Members Summary");
            System.out.println("3. Active Loans Report");
            System.out.println("4. Overdue Loans Report");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateBooksSummary();
                    break;
                case 2:
                    generateMembersSummary();
                    break;
                case 3:
                    generateActiveLoansReport();
                    break;
                case 4:
                    generateOverdueLoansReport();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateBooksSummary() {
        System.out.println("\n--- Books Summary Report ---");
        List<Book> books = bookService.listAllBooks();
        int totalBooks = books.size();
        int totalCopies = books.stream().mapToInt(Book::getTotalCopies).sum();
        int availableCopies = books.stream().mapToInt(Book::getAvailableCopies).sum();
        int loanedCopies = totalCopies - availableCopies;
        System.out.println("Total unique books: " + totalBooks);
        System.out.println("Total copies: " + totalCopies);
        System.out.println("Available copies: " + availableCopies);
        System.out.println("Loaned copies: " + loanedCopies);
        System.out.println("Utilization rate: " +
                String.format("%.2f%%", (double) loanedCopies / totalCopies * 100));
    }

    private static void generateMembersSummary() {
        System.out.println("\n--- Members Summary Report ---");
        List<Member> members = memberService.listAllMembers();
        System.out.println("Total registered members: " + members.size());
    }

    private static void generateActiveLoansReport() {
        System.out.println("\n--- Active Loans Report ---");
        List<Loan> loans = loanService.listAllLoans();
        long activeLoans = loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
        System.out.println("Total active loans: " + activeLoans);
        if (activeLoans > 0) {
            System.out.println("\nActive Loans Details:");
            loans.stream()
                    .filter(loan -> loan.getReturnDate() == null)
                    .forEach(loan -> System.out.println(loan));
        }
    }

    private static void generateOverdueLoansReport() {
        System.out.println("\n--- Overdue Loans Report ---");
        List<Loan> loans = loanService.listAllLoans();
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .filter(loan -> loan.getDueDate() != null && loan.getDueDate().isBefore(today))
                .toList();

        System.out.println("Total overdue loans: " + overdueLoans.size());
        if (!overdueLoans.isEmpty()) {
            System.out.println("\nOverdue Loans Details:");
            for (Loan loan : overdueLoans) {
                long daysOverdue = today.toEpochDay()- loan.getDueDate().toEpochDay();
                System.out.println(loan + " (Overdue by " + daysOverdue + " days)");
            }
        }
    }

    private static String getStringInput(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    private static int getIntInput(String prompt){
        while(true){
            try{
                System.out.print(prompt);
                int value =Integer.parseInt(scanner.nextLine().trim());
                return value;
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double getDoubleInput(String prompt){
        while(true){
            try{
                System.out.print(prompt);
                double value =Double.parseDouble(scanner.nextLine().trim());
                return value;
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }
    private static String truncate(String str,int maxLength){
        if(str ==null)return"";
        return str.length()> maxLength ? str.substring(0, maxLength -3)+"...": str;
    }
}

 */


/*
package com.librarymgmt;

import com.librarymgmt.model.*;
import com.librarymgmt.service.*;
import com.librarymgmt.util.DatabaseHelper;
import com.librarymgmt.util.IdGenerator;
import com.librarymgmt.validation.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Single Scanner instance for the entire app
    private static final Scanner scanner = new Scanner(System.in);

    // Services
    private static final BookService bookService = new BookService();
    private static final MemberService memberService = new MemberService();
    private static final LoanService loanService = new LoanService();
    private static final FineService fineService = new FineService();
    private static final StaffService staffService = new StaffService();
    private static final CategoryService categoryService = new CategoryService();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("           LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        // Initialize SQLite schema (library.db)
        DatabaseHelper.initializeDatabase();

        // App loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> manageBooks();
                case 2 -> manageMembers();
                case 3 -> manageLoans();
                case 4 -> manageFines();
                case 5 -> manageStaff();
                case 6 -> manageCategories();
                case 7 -> generateReports();
                case 0 -> {
                    System.out.println("Thank you for using Library Management System!");
                    // Do not close System.in-backed Scanner in case IDE reuses console.
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Members");
        System.out.println("3. Manage Loans");
        System.out.println("4. Manage Fines");
        System.out.println("5. Manage Staff");
        System.out.println("6. Manage Categories");
        System.out.println("7. Generate Reports");
        System.out.println("0. Exit");
        System.out.println("==================");
    }

    private static void manageBooks() {
        while (true) {
            System.out.println("\n=== BOOK MANAGEMENT ===");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book by ISBN");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addNewBook();
                case 2 -> viewAllBooks();
                case 3 -> searchBookByIsbn();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageMembers() {
        while (true) {
            System.out.println("\n=== MEMBER MANAGEMENT ===");
            System.out.println("1. Add New Member");
            System.out.println("2. View All Members");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addNewMember();
                case 2 -> viewAllMembers();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageLoans() {
        while (true) {
            System.out.println("\n=== LOAN MANAGEMENT ===");
            System.out.println("1. Issue Book Loan");
            System.out.println("2. Return Book");
            System.out.println("3. View All Loans");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> issueBookLoan();
                case 2 -> returnBook();
                case 3 -> viewAllLoans();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageFines() {
        while (true) {
            System.out.println("\n=== FINE MANAGEMENT ===");
            System.out.println("1. Issue Fine");
            System.out.println("2. View All Fines");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> issueFine();
                case 2 -> fineService.viewFines();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageStaff() {
        while (true) {
            System.out.println("\n=== STAFF MANAGEMENT ===");
            System.out.println("1. Add Staff");
            System.out.println("2. View All Staff");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addStaff();
                case 2 -> staffService.viewStaff();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageCategories() {
        while (true) {
            System.out.println("\n=== CATEGORY MANAGEMENT ===");
            System.out.println("1. Add Category");
            System.out.println("2. View All Categories");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> addCategory();
                case 2 -> categoryService.viewCategories();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateReports() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Books Summary");
            System.out.println("2. Members Summary");
            System.out.println("3. Active Loans Report");
            System.out.println("4. Overdue Loans Report");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> generateBooksSummary();
                case 2 -> generateMembersSummary();
                case 3 -> generateActiveLoansReport();
                case 4 -> generateOverdueLoansReport();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static void addNewBook() {
        System.out.println("\n--- Add New Book ---");

        String isbn = getStringInput("Enter ISBN: ");
        if (!InputValidator.isNonEmpty(isbn)) {
            System.out.println("ISBN cannot be empty!");
            return;
        }

        String title = getStringInput("Enter Title: ");
        if (!InputValidator.isNonEmpty(title)) {
            System.out.println("Title cannot be empty!");
            return;
        }

        String author = getStringInput("Enter Author: ");
        if (!InputValidator.isNonEmpty(author)) {
            System.out.println("Author cannot be empty!");
            return;
        }

        String category = getStringInput("Enter Category: ");

        int totalCopies = getIntInput("Enter Total Copies: ");
        if (totalCopies <= 0) {
            System.out.println("Total copies must be greater than 0!");
            return;
        }

        Book book = new Book(isbn, title, author, category, totalCopies, totalCopies);
        bookService.addBook(book);
    }

    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookService.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }

        System.out.printf("%-15s %-30s %-20s %-15s %-8s %-10s%n",
                "ISBN", "Title", "Author", "Category", "Total", "Available");
        System.out.println("=".repeat(105));
        for (Book book : books) {
            System.out.printf("%-15s %-30s %-20s %-15s %-8d %-10d%n",
                    book.getIsbn(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getCategory(), 15),
                    book.getTotalCopies(),
                    book.getAvailableCopies());
        }
    }

    private static void searchBookByIsbn() {
        System.out.println("\n--- Search Book by ISBN ---");
        String isbn = getStringInput("Enter ISBN: ");
        Book book = bookService.findByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found with ISBN: " + isbn);
        } else {
            System.out.println("Book found:");
            System.out.println(book);
        }
    }



    private static void addNewMember() {
        System.out.println("\n--- Add New Member ---");
        String name = getStringInput("Enter Name: ");
        if (!InputValidator.isNonEmpty(name)) {
            System.out.println("Name cannot be empty!");
            return;
        }

        String email = getStringInput("Enter Email: ");
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        String phone = getStringInput("Enter Phone: ");
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("Invalid phone format!");
            return;
        }

        memberService.createMember(name, email, phone);
    }

    private static void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        List<Member> members = memberService.listAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.printf("%-15s %-25s %-30s %-15s%n",
                "Member ID", "Name", "Email", "Phone");
        System.out.println("=".repeat(90));
        for (Member member : members) {
            System.out.printf("%-15s %-25s %-30s %-15s%n",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    truncate(member.getEmail(), 30),
                    member.getPhone());
        }
    }



    private static void issueBookLoan() {
        System.out.println("\n--- Issue Book Loan ---");
        String isbn = getStringInput("Enter Book ISBN: ");
        String memberId = getStringInput("Enter Member ID: ");
        int loanDays = getIntInput("Enter Loan Duration (days, minimum 7): ");

        if (loanDays < 7) {
            System.out.println("Minimum loan duration is 7 days!");
            return;
        }

        boolean success = loanService.loanBook(isbn, memberId, loanDays);
        if (success) {
            System.out.println("Book loan issued successfully!");
        }
    }

    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        String loanId = getStringInput("Enter Loan ID: ");
        boolean success = loanService.recordReturn(loanId);
        if (success) {
            System.out.println("Book return recorded successfully!");
        }
    }

    private static void viewAllLoans() {
        System.out.println("\n--- All Loans ---");
        List<Loan> loans = loanService.listAllLoans();
        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        System.out.printf("%-15s %-15s %-15s %-12s %-12s %-12s%n",
                "Loan ID", "Book ID", "Member ID", "Loan Date", "Due Date", "Return Date");
        System.out.println("=".repeat(95));
        for (Loan loan : loans) {
            System.out.printf("%-15s %-15s %-15s %-12s %-12s %-12s%n",
                    loan.getLoanId(),
                    loan.getBookId(),
                    loan.getMemberId(),
                    loan.getLoanDate() != null ? loan.getLoanDate().toString() : "-",
                    loan.getDueDate() != null ? loan.getDueDate().toString() : "-",
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Not Returned");
        }
    }



    private static void issueFine() {
        System.out.println("\n--- Issue Fine ---");
        String memberId = getStringInput("Enter Member ID: ");
        double amount = getDoubleInput("Enter Fine Amount: ");
        String reason = getStringInput("Enter Reason: ");

        if (amount <= 0) {
            System.out.println("Fine amount must be greater than 0!");
            return;
        }

        String fineId = IdGenerator.generate("FN");
        Fine fine = new Fine(fineId, memberId, amount, reason, LocalDate.now(), false);
        fineService.issueFine(fine);
    }



    private static void addStaff() {
        System.out.println("\n--- Add Staff ---");
        String name = getStringInput("Enter Name: ");
        String role = getStringInput("Enter Role: ");
        String email = getStringInput("Enter Email: ");
        String phone = getStringInput("Enter Phone: ");

        if (!InputValidator.isNonEmpty(name) || !InputValidator.isNonEmpty(role)) {
            System.out.println("Name and role cannot be empty!");
            return;
        }
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("Invalid phone format!");
            return;
        }

        String staffId = IdGenerator.generate("ST");
        Staff staff = new Staff(staffId, name, role, email, phone);
        staffService.addStaff(staff);
    }



    private static void addCategory() {
        System.out.println("\n--- Add Category ---");
        String name = getStringInput("Enter Category Name: ");
        String description = getStringInput("Enter Description: ");

        if (!InputValidator.isNonEmpty(name)) {
            System.out.println("Category name cannot be empty!");
            return;
        }

        String categoryId = IdGenerator.generate("CAT");
        Category category = new Category(categoryId, name, description);
        categoryService.addCategory(category);
    }



    private static void generateBooksSummary() {
        System.out.println("\n--- Books Summary Report ---");
        List<Book> books = bookService.listAllBooks();
        int totalBooks = books.size();
        int totalCopies = books.stream().mapToInt(Book::getTotalCopies).sum();
        int availableCopies = books.stream().mapToInt(Book::getAvailableCopies).sum();
        int loanedCopies = totalCopies - availableCopies;

        System.out.println("Total unique books: " + totalBooks);
        System.out.println("Total copies: " + totalCopies);
        System.out.println("Available copies: " + availableCopies);
        System.out.println("Loaned copies: " + loanedCopies);
        System.out.println("Utilization rate: " +
                String.format("%.2f%%", totalCopies == 0 ? 0.0 : (double) loanedCopies / totalCopies * 100.0));
    }

    private static void generateMembersSummary() {
        System.out.println("\n--- Members Summary Report ---");
        List<Member> members = memberService.listAllMembers();
        System.out.println("Total registered members: " + members.size());
    }

    private static void generateActiveLoansReport() {
        System.out.println("\n--- Active Loans Report ---");
        List<Loan> loans = loanService.listAllLoans();
        long activeLoans = loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
        System.out.println("Total active loans: " + activeLoans);
        if (activeLoans > 0) {
            System.out.println("\nActive Loans Details:");
            loans.stream()
                    .filter(loan -> loan.getReturnDate() == null)
                    .forEach(System.out::println);
        }
    }

    private static void generateOverdueLoansReport() {
        System.out.println("\n--- Overdue Loans Report ---");
        List<Loan> loans = loanService.listAllLoans();
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .filter(loan -> loan.getDueDate() != null && loan.getDueDate().isBefore(today))
                .toList();

        System.out.println("Total overdue loans: " + overdueLoans.size());
        if (!overdueLoans.isEmpty()) {
            System.out.println("\nOverdue Loans Details:");
            for (Loan loan : overdueLoans) {
                long daysOverdue = today.toEpochDay() - loan.getDueDate().toEpochDay();
                System.out.println(loan + " (Overdue by " + daysOverdue + " days)");
            }
        }
    }



    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, Math.max(0, maxLength - 3)) + "..." : str;
    }
}
*/


package com.librarymgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}























