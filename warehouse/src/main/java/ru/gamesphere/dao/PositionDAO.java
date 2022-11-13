package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.domain.Position;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class PositionDAO extends DAO<Position> {

    public PositionDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @NotNull Optional<Position> get(long id) {
        try (var statement = connection.prepareStatement(
                "SELECT position_id, shipping_list_id, product_id, price, amount FROM position WHERE product_id = ?"
        )) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Position.builder()
                            .positionId(resultSet.getLong("position_id"))
                            .shippingListId(resultSet.getLong("shipping_list_id"))
                            .productId(resultSet.getLong("product_id"))
                            .price(resultSet.getBigDecimal("price"))
                            .amount(resultSet.getInt("amount"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Position save(@NotNull Position entity) {
        try (var statement = connection.prepareStatement(
                "INSERT INTO position(shipping_list_id, product_id, price, amount) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, entity.getShippingListId());
            statement.setLong(2, entity.getProductId());
            statement.setBigDecimal(3, entity.getPrice());
            statement.setInt(4, entity.getAmount());
            statement.executeUpdate();

            try (var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    entity.setPositionId(resultSet.getLong("position_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean update(@NotNull Position entity) {
        try (var statement = connection.prepareStatement(
                "UPDATE position SET shipping_list_id = ?, product_id = ?, price = ?, amount = ? WHERE position_id = ?"
        )) {
            statement.setLong(1, entity.getShippingListId());
            statement.setLong(2, entity.getProductId());
            statement.setBigDecimal(3, entity.getPrice());
            statement.setInt(4, entity.getAmount());
            statement.setLong(5, entity.getPositionId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(@NotNull Position entity) {
        try (var statement = connection.prepareStatement(
                "DELETE FROM product WHERE product_id = ?"
        )) {
            statement.setLong(1, entity.getPositionId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
