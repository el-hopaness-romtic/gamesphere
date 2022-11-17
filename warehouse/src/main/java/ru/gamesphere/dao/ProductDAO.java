package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.domain.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ProductDAO extends DAO<Product> {

    public ProductDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @NotNull Optional<Product> get(long id) {
        try (var statement = connection.prepareStatement(
                "SELECT product_id, name FROM product WHERE product_id = ?"
        )) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Product.builder()
                            .productId(resultSet.getLong("product_id"))
                            .name(resultSet.getString("name"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Product save(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(
                "INSERT INTO product(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            try (var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    entity.setProductId(resultSet.getLong("product_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean update(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(
                "UPDATE product SET name = ? WHERE product_id = ?"
        )) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getProductId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(
                "DELETE FROM product WHERE product_id = ?"
        )) {
            statement.setLong(1, entity.getProductId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
