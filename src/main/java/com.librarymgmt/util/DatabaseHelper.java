package com.librarymgmt.util;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.librarymgmt.model.Book;
import com.librarymgmt.model.Member;
import com.librarymgmt.model.Loan;
import com.librarymgmt.model.Fine;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS books (
                  isbn TEXT PRIMARY KEY,
                  title TEXT,
                  author TEXT,
                  category TEXT,
                  total_copies INTEGER,
                  available_copies INTEGER
                );
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS members (
                  member_id TEXT PRIMARY KEY,
                  name TEXT,
                  email TEXT,
                  phone TEXT
                );
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS loans (
                  loan_id TEXT PRIMARY KEY,
                  book_id TEXT,
                  member_id TEXT,
                  loan_date TEXT,
                  due_date TEXT,
                  return_date TEXT,
                  FOREIGN KEY(book_id) REFERENCES books(isbn),
                  FOREIGN KEY(member_id) REFERENCES members(member_id)
                );
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fines (
                  fine_id TEXT PRIMARY KEY,
                  member_id TEXT,
                  amount REAL,
                  reason TEXT,
                  issue_date TEXT,
                  paid INTEGER
                );
                """);

            System.out.println("Database initialized (library.db).");
        } catch (SQLException e) {
            System.err.println("DB init error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /* -------------------- Books -------------------- */
    public static void saveBook(Book book) throws SQLException {
        String upsert = "INSERT OR REPLACE INTO books (isbn, title, author, category, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(upsert)) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.executeUpdate();
        }
    }

    public static List<Book> fetchAllBooks() throws SQLException {
        List<Book> list = new ArrayList<>();
        String q = "SELECT isbn, title, author, category, total_copies, available_copies FROM books";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                Book b = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                );
                list.add(b);
            }
        }
        return list;
    }

    public static Book fetchBookByIsbn(String isbn) throws SQLException {
        String q = "SELECT isbn, title, author, category, total_copies, available_copies FROM books WHERE isbn = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getInt("total_copies"),
                            rs.getInt("available_copies")
                    );
                }
            }
        }
        return null;
    }

    /* -------------------- Members -------------------- */
    public static void saveMember(Member m) throws SQLException {
        String upsert = "INSERT OR REPLACE INTO members (member_id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(upsert)) {
            ps.setString(1, m.getMemberId());
            ps.setString(2, m.getName());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.executeUpdate();
        }
    }

    public static List<Member> fetchAllMembers() throws SQLException {
        List<Member> list = new ArrayList<>();
        String q = "SELECT member_id, name, email, phone FROM members";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                list.add(new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        }
        return list;
    }

    /* -------------------- Loans (transactional) -------------------- */
    /**
     * create a loan: only if the book has available copies > 0; this operation is transactional:
     * insert loan AND decrement available_copies in books in same transaction.
     */
    public static boolean saveLoan(Loan loan) throws SQLException {
        String check = "SELECT available_copies FROM books WHERE isbn = ?";
        String updateBook = "UPDATE books SET available_copies = available_copies - 1 WHERE isbn = ?";
        String insertLoan = "INSERT INTO loans (loan_id, book_id, member_id, loan_date, due_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psCheck = conn.prepareStatement(check)) {
                psCheck.setString(1, loan.getBookId());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false; // book not found
                    }
                    int avail = rs.getInt("available_copies");
                    if (avail <= 0) {
                        conn.rollback();
                        return false; // no copies available
                    }
                }
            }

            try (PreparedStatement psUpd = conn.prepareStatement(updateBook)) {
                psUpd.setString(1, loan.getBookId());
                psUpd.executeUpdate();
            }

            try (PreparedStatement psIns = conn.prepareStatement(insertLoan)) {
                psIns.setString(1, loan.getLoanId());
                psIns.setString(2, loan.getBookId());
                psIns.setString(3, loan.getMemberId());
                psIns.setString(4, loan.getLoanDate() == null ? null : loan.getLoanDate().toString());
                psIns.setString(5, loan.getDueDate() == null ? null : loan.getDueDate().toString());
                psIns.setString(6, loan.getReturnDate() == null ? null : loan.getReturnDate().toString());
                psIns.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public static boolean recordReturn(String loanId) throws SQLException {
        String qLoan = "SELECT book_id, return_date FROM loans WHERE loan_id = ?";
        String updLoan = "UPDATE loans SET return_date = ? WHERE loan_id = ?";
        String updBook = "UPDATE books SET available_copies = available_copies + 1 WHERE isbn = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            String bookId;
            try (PreparedStatement ps = conn.prepareStatement(qLoan)) {
                ps.setString(1, loanId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    bookId = rs.getString("book_id");
                    String existingReturn = rs.getString("return_date");
                    if (existingReturn != null && !existingReturn.isBlank()) {
                        // already returned
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement psUpdLoan = conn.prepareStatement(updLoan)) {
                psUpdLoan.setString(1, java.time.LocalDate.now().toString());
                psUpdLoan.setString(2, loanId);
                psUpdLoan.executeUpdate();
            }

            try (PreparedStatement psUpdBook = conn.prepareStatement(updBook)) {
                psUpdBook.setString(1, bookId);
                psUpdBook.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    /* -------------------- Loans fetch -------------------- */
    public static List<Loan> fetchAllLoans() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String q = "SELECT loan_id, book_id, member_id, loan_date, due_date, return_date FROM loans";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                Loan l = new Loan(
                        rs.getString("loan_id"),
                        rs.getString("book_id"),
                        rs.getString("member_id"),
                        DateUtils.fromString(rs.getString("loan_date")),
                        DateUtils.fromString(rs.getString("due_date")),
                        DateUtils.fromString(rs.getString("return_date"))
                );
                list.add(l);
            }
        }
        return list;
    }

    /* -------------------- Fines -------------------- */
    public static void saveFine(Fine fine) throws SQLException {
        String upsert = "INSERT OR REPLACE INTO fines (fine_id, member_id, amount, reason, issue_date, paid) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(upsert)) {
            ps.setString(1, fine.getFineId());
            ps.setString(2, fine.getMemberId());
            ps.setDouble(3, fine.getAmount());
            ps.setString(4, fine.getReason());
            ps.setString(5, fine.getIssueDate() == null ? null : fine.getIssueDate().toString());
            ps.setInt(6, fine.isPaid() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public static List<Fine> fetchAllFines() throws SQLException {
        List<Fine> list = new ArrayList<>();
        String q = "SELECT fine_id, member_id, amount, reason, issue_date, paid FROM fines";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                Fine f = new Fine(
                        rs.getString("fine_id"),
                        rs.getString("member_id"),
                        rs.getDouble("amount"),
                        rs.getString("reason"),
                        DateUtils.fromString(rs.getString("issue_date")),
                        rs.getInt("paid") == 1
                );
                list.add(f);
            }
        }
        return list;
    }
}
