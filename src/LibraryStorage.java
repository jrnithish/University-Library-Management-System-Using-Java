import java.io.*;
import java.util.ArrayList;

public class LibraryStorage {

    private static final String FILE_NAME = "books.txt";

    public static void saveBooks(ArrayList<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.println(
                    book.getBookId() + "|" +
                    book.getTitle() + "|" +
                    book.getAuthor() + "|" +
                    book.isAvailable()
                );
            }
            System.out.println("Books saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length == 4) {
                    int id = Integer.parseInt(data[0]);
                    String title = data[1];
                    String author = data[2];
                    boolean available = Boolean.parseBoolean(data[3]);

                    Book book = new Book(id, title, author);
                    book.setAvailable(available);
                    books.add(book);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }

        return books;
    }
}