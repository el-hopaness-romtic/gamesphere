package ru.gamesphere.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.gamesphere.model.Author;
import ru.gamesphere.model.Book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections4.ListUtils.indexOf;
import static ru.gamesphere.service.Library.getFullName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FactoryLibrary {
    @Getter
    private static final Library library = new Library();

    static {
        readBooks("./library/book.csv");
        readAuthors("./library/author.csv");
        readConnections("./library/connection.csv");
    }

    private static void readBooks(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            try (CSVReader csvReader = new CSVReader(br)) {
                List<String> columns = Arrays.asList(csvReader.readNext());
                int idPosition = indexOf(columns, s -> s.equalsIgnoreCase("id")),
                    titlePosition = indexOf(columns, s -> s.equalsIgnoreCase("title")),
                    yearPosition = indexOf(columns, s -> s.equalsIgnoreCase("year"));

                String[] values = csvReader.readNext();
                while (values != null) {
                    library.addBook(Book.builder()
                            .bookId(Long.parseLong(values[idPosition]))
                            .title(values[titlePosition])
                            .year(values[yearPosition].isBlank() ? null : Short.parseShort(values[yearPosition]))
                            .build());

                    values = csvReader.readNext();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private static void readAuthors(String fileName) {
        Set<String> names = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            try (CSVReader csvReader = new CSVReader(br)) {
                List<String> columns = Arrays.asList(csvReader.readNext());
                int idPosition = indexOf(columns, s -> s.equalsIgnoreCase("id")),
                    firstnamePosition = indexOf(columns, s -> s.equalsIgnoreCase("firstname")),
                    surnamePosition = indexOf(columns, s -> s.equalsIgnoreCase("surname"));

                String[] values = csvReader.readNext();
                while (values != null) {
                    Author author = Author.builder()
                            .authorId(Long.parseLong(values[idPosition]))
                            .firstname(values[firstnamePosition])
                            .surname(values[surnamePosition].isBlank() ? null : values[surnamePosition])
                            .build();
                    if (!names.add(getFullName(author)))
                        throw new IllegalArgumentException("Met non-unique name in " + fileName + ": " + getFullName(author));

                    library.addAuthor(Author.builder()
                            .authorId(Long.parseLong(values[idPosition]))
                            .firstname(values[firstnamePosition])
                            .surname(values[surnamePosition])
                            .build());

                    values = csvReader.readNext();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private static void readConnections(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            try (CSVReader csvReader = new CSVReader(br)) {
                List<String> columns = Arrays.asList(csvReader.readNext());
                int authorIdPosition = indexOf(columns, s -> s.equalsIgnoreCase("authorId")),
                    bookIdPosition = indexOf(columns, s -> s.equalsIgnoreCase("bookId"));

                String[] values = csvReader.readNext();
                while (values != null) {
                    library.addAuthorship(
                            Long.parseLong(values[authorIdPosition]),
                            Long.parseLong(values[bookIdPosition])
                    );

                    values = csvReader.readNext();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

}
