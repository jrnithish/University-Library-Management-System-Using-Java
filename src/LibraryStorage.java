import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibraryStorage {
    private static final String FILE_NAME = "books.txt";

    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return books;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    Book book = new Book(id, title, author);

                    if (parts.length >= 4) {
                        book.setAvailable(Boolean.parseBoolean(parts[3]));
                    }
                    if (parts.length >= 5 && !parts[4].equals("null")) {
                        book.setBorrowedBy(parts[4]);
                    }
                    // Added: borrowDate handling (now at index 5)
                    if (parts.length >= 6 && !parts[5].equals("null")) {
                        book.setBorrowDate(LocalDate.parse(parts[5]));
                    }
                    // Updated: dueDate handling (now at index 6)
                    if (parts.length >= 7 && !parts[6].equals("null")) {
                        book.setDueDate(LocalDate.parse(parts[6]));
                    }

                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    public static void saveBooks(ArrayList<Book> books) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                String borrowerStr = book.getBorrowedBy() != null ? book.getBorrowedBy() : "null";
                String borrowDateStr = book.getBorrowDate() != null ? book.getBorrowDate().toString() : "null";
                String dueDateStr = book.getDueDate() != null ? book.getDueDate().toString() : "null";

                pw.println(book.getBookId() + "|" +
                           book.getTitle() + "|" +
                           book.getAuthor() + "|" +
                           book.isAvailable() + "|" +
                           borrowerStr + "|" +
                           borrowDateStr + "|" +
                           dueDateStr);
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }
}