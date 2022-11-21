package ru.gamesphere;


import ru.gamesphere.service.ArgsParser;
import ru.gamesphere.service.Report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class Main {
    static {
        Locale.setDefault(new Locale("en", "GB"));
    }

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        try (Connection connection = DriverManager.getConnection(parser.getJdbcUrl(), parser.getUsername(), parser.getPassword())) {
            Report report = new Report(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
