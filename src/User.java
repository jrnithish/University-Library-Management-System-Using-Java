import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private String studentName;

    public User(String studentName) {
        this.studentName = studentName;
    }

    // Count books currently borrowed by this student
    public int countBorrowedBooks(ArrayList<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (studentName.equals(book.getBorrowedBy())) {
                count++;
            }
        }
        return count;
    }

    // Get list of books borrowed by this student
    public ArrayList<Book> getMyBorrowedBooks(ArrayList<Book> books) {
        ArrayList<Book> myBooks = new ArrayList<>();
        for (Book book : books) {
            if (studentName.equals(book.getBorrowedBy())) {
                myBooks.add(book);
            }
        }
        return myBooks;
    }

    // Search available books by title or author partial match (case-insensitive)
    public ArrayList<Book> searchAvailableBooks(ArrayList<Book> books, String query) {
        ArrayList<Book> results = new ArrayList<>();
        String q = query.toLowerCase();
        for (Book book : books) {
            if (book.isAvailable() &&
                (book.getTitle().toLowerCase().contains(q) ||
                 book.getAuthor().toLowerCase().contains(q))) {
                results.add(book);
            }
        }
        return results;
    }

    // Search books by title or author partial match (case-insensitive)
    public void searchBook(ArrayList<Book> books, String query) {
        boolean found = false;
        String q = query.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(q) ||
                book.getAuthor().toLowerCase().contains(q)) {
                book.displayBook(false, studentName);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // View only this student's borrowed books
    public void viewMyBorrowedBooks(ArrayList<Book> books) {
        ArrayList<Book> myBooks = getMyBorrowedBooks(books);
        boolean found = false;
        System.out.println("\n===== MY BORROWED BOOKS =====");
        for (Book book : myBooks) {
            System.out.println("-------------------------");
            System.out.println("Book ID: " + book.getBookId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Borrow Date: " + book.getBorrowDate());
            System.out.println("Due Date: " + book.getDueDate());
            System.out.println("Fine: $" + String.format("%.2f", book.calculateFine()));
            found = true;
        }
        if (!found) {
            System.out.println("You have not borrowed any books.");
        }
        System.out.println("-------------------------");
    }

    // Borrow a book by title/author query with borrow limit check
    public void borrowBook(ArrayList<Book> books, String query, Scanner sc) {
        // Check borrow limit
        if (countBorrowedBooks(books) >= 3) {
            System.out.println("You have reached the maximum borrow limit of 3 books.");
            return;
        }

        // Find available books matching query
        ArrayList<Book> matches = searchAvailableBooks(books, query);
        if (matches.isEmpty()) {
            System.out.println("No available books found matching your search.");
            return;
        }

        // Display matches with index
        System.out.println("\n===== AVAILABLE BOOKS =====");
        for (int i = 0; i < matches.size(); i++) {
            Book book = matches.get(i);
            System.out.println((i + 1) + ". Book ID: " + book.getBookId() + " - " + book.getTitle() + " by " + book.getAuthor());
        }
        System.out.println("-------------------------");

        System.out.print("Enter book number to borrow or 0 to cancel: ");
        if (sc.hasNextInt()) {
            int selection = sc.nextInt();
            sc.nextLine();
            if (selection >= 1 && selection <= matches.size()) {
                Book selected = matches.get(selection - 1);
                selected.setAvailable(false);
                selected.setBorrowedBy(studentName);
                selected.setBorrowDate(LocalDate.now());
                selected.setDueDate(selected.getBorrowDate().plusDays(14));
                System.out.println("Book borrowed successfully.");
            } else if (selection == 0) {
                System.out.println("Borrow cancelled.");
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    // Return a book: list only this student's borrowed books (1-based index),
    // return by list number or book title, clearing borrower/borrow/due dates.
    public void returnBook(ArrayList<Book> books, Scanner sc) {
        ArrayList<Book> myBooks = getMyBorrowedBooks(books);
        if (myBooks.isEmpty()) {
            System.out.println("You have not borrowed any books.");
            return;
        }

        System.out.println("\n===== MY BORROWED BOOKS =====");
        for (int i = 0; i < myBooks.size(); i++) {
            Book book = myBooks.get(i);
            System.out.println((i + 1) + ". Book ID: " + book.getBookId() + " - " + book.getTitle() + " by " + book.getAuthor());
        }
        System.out.println("-------------------------");

        System.out.print("Enter book number or title to return: ");
        String input = sc.nextLine().trim();

        Book selected = null;
        try {
            int index = Integer.parseInt(input);
            if (index >= 1 && index <= myBooks.size()) {
                selected = myBooks.get(index - 1);
            } else {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            for (Book book : myBooks) {
                if (book.getTitle().equalsIgnoreCase(input)) {
                    selected = book;
                    break;
                }
            }
            if (selected == null) {
                System.out.println("Book not found in your borrowed list.");
                return;
            }
        }

        selected.setAvailable(true);
        double fine = selected.calculateFine();
        if (fine > 0) {
            System.out.printf("Book returned successfully. Late fee: $%.2f%n", fine);
        } else {
            System.out.println("Book returned successfully.");
        }
        selected.setBorrowedBy(null);
        selected.setBorrowDate(null);
        selected.setDueDate(null);
    }
}