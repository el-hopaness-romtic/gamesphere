package ru.gamesphere;

import org.flywaydb.core.Flyway;

import java.util.Locale;

public class Main {
    static {
        Locale.setDefault(new Locale("en", "GB"));
    }

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        Flyway.configure()
                .dataSource(parser.getJdbcUrl(),
                        parser.getUsername(),
                        parser.getPassword())
                .locations("db")
                .load()
                .migrate();
    }
}
