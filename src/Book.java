import java.time.LocalDate;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean available = true;
    private String borrowedBy;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.borrowedBy = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrower) {
        this.borrowedBy = borrower;
    }

    public void displayBook(boolean isAdmin, String studentName) {
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Status: " + (available ? "Available" : "Borrowed"));
        if (isAdmin) {
            if (borrowedBy != null) {
                System.out.println("Borrower Name: " + borrowedBy);
                System.out.println("Borrow Date: " + borrowDate);
                System.out.println("Due Date: " + dueDate);
                System.out.println("Fine: $" + String.format("%.2f", calculateFine()));
            }
        } else {
            // Student view - only show details if borrowed by this student
            if (borrowedBy != null && borrowedBy.equals(studentName)) {
                System.out.println("Borrower Name: " + borrowedBy);
                System.out.println("Borrow Date: " + borrowDate);
                System.out.println("Due Date: " + dueDate);
                System.out.println("Fine: $" + String.format("%.2f", calculateFine()));
            }
        }
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double calculateFine() {
        if (dueDate != null && LocalDate.now().isAfter(dueDate)) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysLate * 1.0;
        }
        return 0.0;
    }
}