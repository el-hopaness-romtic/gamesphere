package ru.gamesphere;

import lombok.Getter;

@Getter
public class ArgsParser {
    public static final String JDBC_URL_ARGUMENT = "--jdbcUrl=";
    public static final String USERNAME_ARGUMENT = "--user=";
    public static final String PASSWORD_ARGUMENT = "--pswd=";

    private String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String password = "12345678";

    ArgsParser(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith(JDBC_URL_ARGUMENT)) {
                jdbcUrl = args[i].substring(JDBC_URL_ARGUMENT.length());
            } else if (args[i].startsWith(USERNAME_ARGUMENT)) {
                username = args[i].substring(USERNAME_ARGUMENT.length());
            } else if (args[i].startsWith(PASSWORD_ARGUMENT)) {
                password = args[i].substring(PASSWORD_ARGUMENT.length());
            }
        }

    }
}
