package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.domain.ShippingList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ShippingListDAO extends DAO<ShippingList> {

    public ShippingListDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @NotNull Optional<ShippingList> get(long id) {
        try (var statement = connection.prepareStatement(
                "SELECT shipping_list_id, shipping_date, organization_id FROM shipping_list WHERE shipping_list_id = ?"
        )) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(ShippingList.builder()
                            .shippingListId(resultSet.getLong("shipping_list_id"))
                            .shippingDate(resultSet.getDate("shipping_date").toLocalDate())
                            .organizationId(resultSet.getLong("organization_id"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull ShippingList save(@NotNull ShippingList entity) {
        try (var statement = connection.prepareStatement(
                "INSERT INTO shipping_list(shipping_date, organization_id) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setDate(1, Date.valueOf(entity.getShippingDate()));
            statement.setLong(2, entity.getOrganizationId());
            statement.executeUpdate();

            try (var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    entity.setShippingListId(resultSet.getLong("shipping_list_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean update(@NotNull ShippingList entity) {
        try (var statement = connection.prepareStatement(
                "UPDATE shipping_list SET shipping_date = ?, organization_id = ? WHERE shipping_list_id = ?"
        )) {
            statement.setDate(1, Date.valueOf(entity.getShippingDate()));
            statement.setLong(2, entity.getOrganizationId());
            statement.setLong(3, entity.getShippingListId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(@NotNull ShippingList entity) {
        try (var statement = connection.prepareStatement(
                "DELETE FROM shipping_list WHERE shipping_list_id = ?"
        )) {
            statement.setLong(1, entity.getShippingListId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
