
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {

    // Add a book
    public void addBook(ArrayList<Book> books, Book book) {
        for (Book existing : books) {
            if (existing.getBookId() == book.getBookId()) {
                System.out.println("Book ID already exists!");
                return;
            }
        }

        books.add(book);
        System.out.println("Book added successfully.");
    }

    // View all books
    public void viewBooks(ArrayList<Book> books, boolean isAdmin, String studentName) {
        System.out.println("\n===== LIBRARY BOOKS =====");

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        for (Book book : books) {
            System.out.println("-------------------------");
            System.out.println("Book ID: " + book.getBookId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Status: " + (book.isAvailable() ? "Available" : "Borrowed"));
            if (isAdmin) {
                if (!book.isAvailable() && book.getBorrowedBy() != null) {
                    System.out.println("Borrower Name: " + book.getBorrowedBy());
                    System.out.println("Borrow Date: " + book.getBorrowDate());
                    System.out.println("Due Date: " + book.getDueDate());
                    System.out.println("Fine: $" + String.format("%.2f", book.calculateFine()));
                }
            } else {
                // Student view - only show details for own borrowed books
                if (!book.isAvailable() && book.getBorrowedBy() != null && book.getBorrowedBy().equals(studentName)) {
                    System.out.println("Borrower Name: " + book.getBorrowedBy());
                    System.out.println("Borrow Date: " + book.getBorrowDate());
                    System.out.println("Due Date: " + book.getDueDate());
                    System.out.println("Fine: $" + String.format("%.2f", book.calculateFine()));
                }
            }
        }
        System.out.println("-------------------------");
    }

    // Search books by title or author partial match (case-insensitive)
    public void searchBook(ArrayList<Book> books, String title, boolean isAdmin, String studentName) {
        boolean found = false;
        String q = title.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(q) ||
                book.getAuthor().toLowerCase().contains(q)) {
                System.out.println("\nBook Found!");
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Status: "
                        + (book.isAvailable() ? "Available" : "Borrowed"));
                if (isAdmin) {
                    if (!book.isAvailable() && book.getBorrowedBy() != null) {
                        System.out.println("Borrower Name: " + book.getBorrowedBy());
                        System.out.println("Borrow Date: " + book.getBorrowDate());
                        System.out.println("Due Date: " + book.getDueDate());
                        System.out.println("Fine: $" + String.format("%.2f", book.calculateFine()));
                    }
                } else {
                    // Student view - only show details for own borrowed books
                    if (!book.isAvailable() && book.getBorrowedBy() != null && book.getBorrowedBy().equals(studentName)) {
                        System.out.println("Borrower Name: " + book.getBorrowedBy());
                        System.out.println("Borrow Date: " + book.getBorrowDate());
                        System.out.println("Due Date: " + book.getDueDate());
                        System.out.println("Fine: $" + String.format("%.2f", book.calculateFine()));
                    }
                }
                found = true;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // Count available books
    public void countAvailableBooks(ArrayList<Book> books) {
        int count = 0;

        for (Book book : books) {
            if (book.isAvailable()) {
                count++;
            }
        }

        System.out.println("Available books: " + count);
        System.out.println("Total books: " + books.size());
    }

    // Add a new book using keyboard input
    public void addNewBook(ArrayList<Book> books, Scanner sc) {
        System.out.print("Enter Book ID: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid Book ID. Enter a number.");
            sc.nextLine();
            return;
        }

        int bookId = sc.nextInt();
        sc.nextLine();

        for (Book book : books) {
            if (book.getBookId() == bookId) {
                System.out.println("Book ID already exists!");
                return;
            }
        }

        System.out.print("Enter Book Title: ");
        String title = sc.nextLine().trim();

        System.out.print("Enter Author Name: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }

        books.add(new Book(bookId, title, author));
        System.out.println("New book added successfully!");
    }

    // Remove a book by ID
    public void removeBook(ArrayList<Book> books, Scanner sc) {
        System.out.print("Enter Book ID to remove: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid Book ID. Enter a number.");
            sc.nextLine();
            return;
        }

        int bookId = sc.nextInt();
        sc.nextLine();

        for (Book book : books) {
            if (book.getBookId() == bookId) {
                if (!book.isAvailable()) {
                    System.out.println(
                            "Cannot remove this book because it is borrowed.");
                    return;
                }

                books.remove(book);
                System.out.println("Book removed successfully!");
                return;
            }
        }

        System.out.println("Book ID not found.");
    }
}
