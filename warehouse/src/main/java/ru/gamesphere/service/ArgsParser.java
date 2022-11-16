package ru.gamesphere.service;

import lombok.Getter;

@Getter
public class ArgsParser {
    public static final String JDBC_URL_ARGUMENT = "--jdbcUrl=";
    public static final String USERNAME_ARGUMENT = "--user=";
    public static final String PASSWORD_ARGUMENT = "--pswd=";

    private String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String password = "12345678";

    public ArgsParser(String[] args) {
        for (String arg : args) {
            if (arg.startsWith(JDBC_URL_ARGUMENT)) {
                jdbcUrl = arg.substring(JDBC_URL_ARGUMENT.length());
            } else if (arg.startsWith(USERNAME_ARGUMENT)) {
                username = arg.substring(USERNAME_ARGUMENT.length());
            } else if (arg.startsWith(PASSWORD_ARGUMENT)) {
                password = arg.substring(PASSWORD_ARGUMENT.length());
            }
        }
    }
}
