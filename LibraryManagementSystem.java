import java.util.*;

public class LibraryManagementSystem {

    // Book class
    static class Book {
        String isbn;
        String title;
        String author;
        String publicationDate;
        String genre;
        boolean isCheckedOut;
        Book next;

        public Book(String isbn, String title, String author, String publicationDate, String genre) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.publicationDate = publicationDate;
            this.genre = genre;
            this.isCheckedOut = false;
            this.next = null;
        }
    }

    // Library class
    static class Library {
        private Book head;
        private Map<String, Book> bookTable;
        private Stack<String> transactionStack;

        public Library() {
            this.head = null;
            this.bookTable = new HashMap<>();
            this.transactionStack = new Stack<>();
        }

        public void addBook(Book book) {
            if (bookTable.containsKey(book.isbn)) {
                System.out.println("ERROR: A book with this ISBN already exists.");
                return;
            }

            if (head == null) {
                head = book;
            } else {
                Book current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = book;
            }

            bookTable.put(book.isbn, book);
            transactionStack.push("[ADDED] " + book.title + " at " + new Date());
            System.out.println("Book added successfully!");
        }

        public void deleteBook(String isbn) {
            if (!bookTable.containsKey(isbn)) {
                System.out.println("Book not found.");
                return;
            }

            Book prev = null;
            Book current = head;
            while (current != null && !current.isbn.equals(isbn)) {
                prev = current;
                current = current.next;
            }

            if (current != null) {
                if (prev != null) {
                    prev.next = current.next;
                } else {
                    head = current.next;
                }
                bookTable.remove(isbn);
                transactionStack.push("[DELETED] " + current.title + " at " + new Date());
                System.out.println("Book deleted successfully.");
            }
        }

        public void updateBook(String isbn, String title, String author, String pubDate, String genre) {
            Book book = bookTable.get(isbn);
            if (book == null) {
                System.out.println("Book not found.");
                return;
            }

            if (!title.isEmpty()) book.title = title;
            if (!author.isEmpty()) book.author = author;
            if (!pubDate.isEmpty()) book.publicationDate = pubDate;
            if (!genre.isEmpty()) book.genre = genre;

            transactionStack.push("[UPDATED] " + book.title + " at " + new Date());
            System.out.println("Book updated successfully.");
        }

        public void checkOutBook(String isbn) {
            Book book = bookTable.get(isbn);
            if (book == null) {
                System.out.println("Book not found.");
            } else if (book.isCheckedOut) {
                System.out.println("Book is already checked out.");
            } else {
                book.isCheckedOut = true;
                transactionStack.push("[CHECKED OUT] " + book.title + " at " + new Date());
                System.out.println(book.title + " has been checked out.");
            }
        }

        public void checkInBook(String isbn) {
            Book book = bookTable.get(isbn);
            if (book == null) {
                System.out.println("Book not found.");
            } else if (!book.isCheckedOut) {
                System.out.println("Book is already checked in.");
            } else {
                book.isCheckedOut = false;
                transactionStack.push("[CHECKED IN] " + book.title + " at " + new Date());
                System.out.println(book.title + " has been checked in.");
            }
        }

        public void displayBooks(String sortBy) {
            List<Book> books = new ArrayList<>();
            Book current = head;
            while (current != null) {
                books.add(current);
                current = current.next;
            }

            Comparator<Book> comparator;
            switch (sortBy.toLowerCase()) {
                case "author":
                    comparator = Comparator.comparing(b -> b.author);
                    break;
                case "publication_date":
                    comparator = Comparator.comparing(b -> b.publicationDate);
                    break;
                case "genre":
                    comparator = Comparator.comparing(b -> b.genre);
                    break;
                default:
                    comparator = Comparator.comparing(b -> b.title);
            }

            books.sort(comparator);
            for (Book b : books) {
                String status = b.isCheckedOut ? "Checked Out" : "Available";
                System.out.println("ISBN: " + b.isbn + " | Title: " + b.title + " | Author: " + b.author +
                        " | Genre: " + b.genre + " | Published: " + b.publicationDate + " | Status: " + status);
            }
        }

        public void searchBook(String isbn) {
            Book book = bookTable.get(isbn);
            if (book == null) {
                System.out.println("Book not found.");
                return;
            }

            String status = book.isCheckedOut ? "Checked Out" : "Available";
            System.out.println("Found: ISBN: " + book.isbn + " | Title: " + book.title +
                    " | Author: " + book.author + " | Genre: " + book.genre +
                    " | Published: " + book.publicationDate + " | Status: " + status);
        }

        public void displayTransactionHistory() {
            System.out.println("\nTransaction History:");
            ListIterator<String> iterator = transactionStack.listIterator(transactionStack.size());
            while (iterator.hasPrevious()) {
                System.out.println(iterator.previous());
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        Library library = new Library();
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book by ISBN");
            System.out.println("4. Check Out Book");
            System.out.println("5. Check In Book");
            System.out.println("6. Delete Book");
            System.out.println("7. Update Book");
            System.out.println("8. View Transaction History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter ISBN: ");
                    String isbn = input.nextLine();
                    System.out.print("Enter Title: ");
                    String title = input.nextLine();
                    System.out.print("Enter Author: ");
                    String author = input.nextLine();
                    System.out.print("Enter Publication Date: ");
                    String pubDate = input.nextLine();
                    System.out.print("Enter Genre: ");
                    String genre = input.nextLine();
                    library.addBook(new Book(isbn, title, author, pubDate, genre));
                    break;

                case "2":
                    System.out.print("Sort by (title/author/publication_date/genre): ");
                    String sortBy = input.nextLine();
                    library.displayBooks(sortBy);
                    break;

                case "3":
                    System.out.print("Enter ISBN to search: ");
                    isbn = input.nextLine();
                    library.searchBook(isbn);
                    break;

                case "4":
                    System.out.print("Enter ISBN to check out: ");
                    isbn = input.nextLine();
                    library.checkOutBook(isbn);
                    break;

                case "5":
                    System.out.print("Enter ISBN to check in: ");
                    isbn = input.nextLine();
                    library.checkInBook(isbn);
                    break;

                case "6":
                    System.out.print("Enter ISBN to delete: ");
                    isbn = input.nextLine();
                    library.deleteBook(isbn);
                    break;

                case "7":
                    System.out.print("Enter ISBN to update: ");
                    isbn = input.nextLine();
                    System.out.print("Enter new Title (or leave blank): ");
                    title = input.nextLine();
                    System.out.print("Enter new Author (or leave blank): ");
                    author = input.nextLine();
                    System.out.print("Enter new Publication Date (or leave blank): ");
                    pubDate = input.nextLine();
                    System.out.print("Enter new Genre (or leave blank): ");
                    genre = input.nextLine();
                    library.updateBook(isbn, title, author, pubDate, genre);
                    break;

                case "8":
                    library.displayTransactionHistory();
                    break;

                case "9":
                    System.out.println("Exiting... Goodbye!");
                    input.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
