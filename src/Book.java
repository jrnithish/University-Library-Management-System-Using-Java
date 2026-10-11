public class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean available = true;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    public int getBookId() {
        return bookId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public String getTitle() {
    return title;
    }

    public String getAuthor() {
    return author;
    }
}