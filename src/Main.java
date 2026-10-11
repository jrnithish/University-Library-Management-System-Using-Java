import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ArrayList<Book> books = LibraryStorage.loadBooks();
        Scanner sc = new Scanner(System.in);

        Admin admin = new Admin();
        User user = new User();

        // Add sample books only when no saved books exist
        if (books.isEmpty()) {
            admin.addBook(books, new Book(1, "Java Programming", "James Gosling"));
            admin.addBook(books, new Book(2, "Python Basics", "Guido van Rossum"));
            LibraryStorage.saveBooks(books);
        }

        int choice;

        do {
            System.out.println("\n===== UNIVERSITY LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. View All Books");
            System.out.println("2. Search Book by Title");
            System.out.println("3. Count Available Books");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Add a New Book");
            System.out.println("7. Remove a Book");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.nextLine();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    admin.viewBooks(books);
                    break;

                case 2:
                    System.out.print("Enter book title to search: ");
                    String title = sc.nextLine();
                    admin.searchBook(books, title);
                    break;

                case 3:
                    admin.countAvailableBooks(books);
                    break;

                case 4:
                    System.out.print("Enter Book ID to borrow: ");
                    if (sc.hasNextInt()) {
                        int borrowId = sc.nextInt();
                        sc.nextLine();
                        user.borrowBook(books, borrowId);
                        LibraryStorage.saveBooks(books);
                    } else {
                        System.out.println("Invalid Book ID.");
                        sc.nextLine();
                    }
                    break;

                case 5:
                    System.out.print("Enter Book ID to return: ");
                    if (sc.hasNextInt()) {
                        int returnId = sc.nextInt();
                        sc.nextLine();
                        user.returnBook(books, returnId);
                        LibraryStorage.saveBooks(books);
                    } else {
                        System.out.println("Invalid Book ID.");
                        sc.nextLine();
                    }
                    break;

                case 6:
                    admin.addNewBook(books, sc);
                    LibraryStorage.saveBooks(books);
                    break;

                case 7:
                    admin.removeBook(books, sc);
                    LibraryStorage.saveBooks(books);
                    break;

                case 8:
                    LibraryStorage.saveBooks(books);
                    System.out.println("Thank you for using the Library Management System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1 to 8.");
            }

        } while (choice != 8);

        sc.close();
    }
}