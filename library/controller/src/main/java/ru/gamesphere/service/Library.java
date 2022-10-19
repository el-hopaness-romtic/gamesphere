package ru.gamesphere.service;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.model.Author;
import ru.gamesphere.model.Book;

import java.util.*;

public class Library {

    private final Map<Long, Book> books = new HashMap<>();
    private final Map<Long, Author> authors = new HashMap<>();
    private final Map<Author, List<Book>> authorships = new HashMap<>();

    void addBook(@NotNull Book book) {
        books.put(book.getBookId(), book);
    }

    void addAuthor(@NotNull Author author) {
        authors.put(author.getAuthorId(), author);
    }

    void addAuthorship(@NotNull Long authorId, @NotNull Long bookId) {
        Author author = authors.get(authorId);
        if (author == null)
            throw new IllegalArgumentException("Author with id = " + authorId + " does not exist");
        Book book = books.get(bookId);
        if (book == null)
            throw new IllegalArgumentException("Book with id = " + bookId + " does not exist");

        List<Book> authorsBooks = authorships.computeIfAbsent(author, k -> new LinkedList<>());
        authorsBooks.add(book);
    }

    @NotNull
    public List<Book> findBooksByAuthorName(@NotNull String name) {
        Author author = authors.values().stream()
                .filter(a ->
                        name.equalsIgnoreCase(getFullName(a))
                ).findFirst()
                .orElse(null);

        return authorships.getOrDefault(author, Collections.emptyList());
    }

    @NotNull
    static String getFullName(@NotNull Author a) {
        return a.getFirstname() + (a.getSurname() == null ? "" : " " + a.getSurname());
    }
}
