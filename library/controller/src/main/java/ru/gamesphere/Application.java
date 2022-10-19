package ru.gamesphere;

import com.google.gson.Gson;
import ru.gamesphere.model.Book;
import ru.gamesphere.service.FactoryLibrary;
import ru.gamesphere.service.Library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        System.out.println("Usage: authorName [authorSurname]");
        String name;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            name = reader.readLine().strip();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Library library = FactoryLibrary.getLibrary();
        List<Book> listItems = library.findBooksByAuthorName(name);

        String jsonStr = new Gson().toJson(listItems);
        System.out.println(jsonStr);
    }
}
