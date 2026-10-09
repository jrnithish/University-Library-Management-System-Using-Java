
import java.util.ArrayList;

public class User {

    // Search books by title
    public void searchBook(ArrayList<Book> books, String title) {
        boolean found = false;

        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.displayBook();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // Borrow a book
    public void borrowBook(ArrayList<Book> books, int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                if (book.isAvailable()) {
                    book.setAvailable(false);
                    System.out.println("Book borrowed successfully.");
                } else {
                    System.out.println("Book is already borrowed.");
                }
                return;
            }
        }

        System.out.println("Invalid Book ID.");
    }

    // Return a book
    public void returnBook(ArrayList<Book> books, int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                if (!book.isAvailable()) {
                    book.setAvailable(true);
                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("This book was not borrowed.");
                }
                return;
            }
        }

        System.out.println("Invalid Book ID.");
    }
}
