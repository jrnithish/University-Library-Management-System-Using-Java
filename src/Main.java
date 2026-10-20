import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) {

        ArrayList<Book> books = LibraryStorage.loadBooks();
        Scanner sc = new Scanner(System.in);

        // Role-based login at startup
        System.out.print("Enter role (1. Admin, 2. Student): ");
        int role = sc.nextInt();
        sc.nextLine();
        boolean isAdmin = (role == 1);

        if (isAdmin) {
            System.out.print("Enter admin password: ");
            String adminPass = sc.nextLine().trim();
            if (!sha256(adminPass).equals(sha256("admin123"))) {
                System.out.println("Incorrect admin password. Access denied.");
                sc.close();
                return;
            }
        }

        String studentName = null;
        User user = null;
        Admin admin = new Admin();

        // Add sample books only when no saved books exist
        if (books.isEmpty()) {
            admin.addBook(books, new Book(1, "Java Programming", "James Gosling"));
            admin.addBook(books, new Book(2, "Python Basics", "Guido van Rossum"));
            LibraryStorage.saveBooks(books);
        }

        // For students, ask for username and password and validate login/registration
        if (!isAdmin) {
            Map<String, String> users = loadUsers();
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.print("Enter your username: ");
                String username = sc.nextLine().trim();

                if (!users.containsKey(username)) {
                    System.out.print("You aren't registered to this library yet. Would you like to register? (y/n): ");
                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("y")) {
                        System.out.print("Create a password: ");
                        String password = sc.nextLine().trim();
                        registerUser(username, sha256(password));
                        users.put(username, sha256(password));
                        System.out.println("Registration successful! You can now use the library system.");
                        studentName = username;
                        user = new User(studentName);
                        loggedIn = true;
                    } else {
                        System.out.println("Registration is required to use the library system. Exiting.");
                        sc.close();
                        return;
                    }
                } else {
                    System.out.print("Enter your password: ");
                    String password = sc.nextLine().trim();
                    if (users.get(username).equals(sha256(password))) {
                        studentName = username;
                        user = new User(studentName);
                        loggedIn = true;
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                        sc.close();
                        return;
                    }
                }
            }
        }

        int choice;

        do {
            System.out.println("\n===== UNIVERSITY LIBRARY MANAGEMENT SYSTEM =====");
            if (isAdmin) {
                System.out.println("1. View All Books");
                System.out.println("2. Add a Book");
                System.out.println("3. Remove a Book");
                System.out.println("4. Search Books / Authors");
                System.out.println("5. Exit");
            } else {
                System.out.println("1. View All Books");
                System.out.println("2. Search Books / Authors");
                System.out.println("3. View My Borrowed Books");
                System.out.println("4. Borrow a Book");
                System.out.println("5. Return a Book");
                System.out.println("6. Exit");
            }
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.nextLine();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    if (isAdmin) {
                        admin.viewBooks(books, true, null);
                    } else {
                        admin.viewBooks(books, false, studentName);
                    }
                    break;

                case 2:
                    if (isAdmin) {
                        admin.addNewBook(books, sc);
                        LibraryStorage.saveBooks(books);
                    } else {
                        System.out.print("Enter search term: ");
                        user.searchBook(books, sc.nextLine().trim());
                    }
                    break;

                case 3:
                    if (isAdmin) {
                        admin.removeBook(books, sc);
                        LibraryStorage.saveBooks(books);
                    } else {
                        user.viewMyBorrowedBooks(books);
                    }
                    break;

                case 4:
                    if (isAdmin) {
                        System.out.print("Enter search term: ");
                        admin.searchBook(books, sc.nextLine().trim(), true, null);
                    } else {
                        System.out.print("Enter book title or author to search: ");
                        String query = sc.nextLine().trim();
                        user.borrowBook(books, query, sc);
                        LibraryStorage.saveBooks(books);
                    }
                    break;

                case 5:
                    if (isAdmin) {
                        System.out.println("Exiting system...");
                    } else {
                        user.returnBook(books, sc);
                        LibraryStorage.saveBooks(books);
                    }
                    break;

                case 6:
                    if (!isAdmin) {
                        LibraryStorage.saveBooks(books);
                        System.out.println("Thank you for using the Library Management System!");
                    } else {
                        System.out.println("Invalid choice for admin role.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (isAdmin ? choice != 5 : choice != 6);

        sc.close();
    }

    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return users;
        }

        boolean dirty = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    String username = parts[0].trim();
                    String password = (parts.length >= 2) ? parts[1].trim() : "";
                    // Auto-migrate: hash any password that isn't already a 64-char SHA-256 hex string
                    if (!password.matches("[0-9a-f]{64}")) {
                        password = sha256(password);
                        dirty = true;
                    }
                    users.put(username, password);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        // Persist migrated (hashed) passwords back to users.txt
        if (dirty) {
            saveUsers(users);
        }
        return users;
    }

    private static void saveUsers(Map<String, String> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                pw.println(entry.getKey() + "|" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private static void registerUser(String username, String hashedPassword) {
        Map<String, String> users = loadUsers();
        users.put(username, hashedPassword);
        saveUsers(users);
    }

    // SHA-256 hash helper returning a 64-character hex string
    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing not available", e);
        }
    }
}