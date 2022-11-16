package ru.gamesphere.service;

import lombok.RequiredArgsConstructor;
import ru.gamesphere.domain.Organization;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Report {

    private final Connection connection;

    List<Organization> top10ByProduct(long productId) {
        ArrayList<Organization> organizations = new ArrayList<>();
        try (var statement = connection.prepareStatement("""
                    SELECT o.*, SUM(p.amount) AS total_amount
                    FROM organization o
                    INNER JOIN shipping_list sl ON sl.organization_id = o.organization_id
                    INNER JOIN position p ON p.shipping_list_id = sl.shipping_list_id AND p.product_id = ?
                    GROUP BY o.organization_id
                    ORDER BY total_amount DESC
                    LIMIT 10
                """)) {
            statement.setLong(1, productId);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    organizations.add(Organization.builder()
                            .organizationId(resultSet.getLong("organization_id"))
                            .accountNumber(resultSet.getString("accountNumber"))
                            .inn(resultSet.getString("inn"))
                            .name(resultSet.getString("name"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return organizations;
    }

}
