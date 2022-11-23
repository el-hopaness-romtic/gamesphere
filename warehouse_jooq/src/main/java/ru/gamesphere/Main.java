package ru.gamesphere;


import ru.gamesphere.service.ArgsParser;
import ru.gamesphere.service.Report;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public class Main {
    static {
        Locale.setDefault(new Locale("en", "GB"));
    }

    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);
        try (Connection connection = DriverManager.getConnection(parser.getJdbcUrl(), parser.getUsername(), parser.getPassword())) {
            Report report = new Report(connection);

            Report.prettyPrint(report.top10ByProduct(1L));

            Report.prettyPrint(report.amountGreaterThanParameter(Map.of(6L, BigDecimal.ONE)));
            Report.prettyPrint(report.amountGreaterThanParameter(Map.of(6L, BigDecimal.valueOf(3))));
            Report.prettyPrint(report.amountGreaterThanParameter(Map.of(12L, BigDecimal.ONE, 13L, BigDecimal.ONE)));

            Report.prettyPrint(report.amountGreaterThanParameter2(LocalDate.of(2022, 4, 20), LocalDate.of(2022, 12, 19)));

            Report.prettyPrint(report.productsAveragePrice(LocalDate.of(2022, 4, 20), LocalDate.of(2022, 12, 19)));

            System.out.println(new String(
                    report.organizationsProducts().toString().getBytes(StandardCharsets.UTF_8)
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
