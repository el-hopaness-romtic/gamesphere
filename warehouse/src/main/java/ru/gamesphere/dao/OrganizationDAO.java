package ru.gamesphere.dao;

import org.jetbrains.annotations.NotNull;
import ru.gamesphere.domain.Organization;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class OrganizationDAO extends DAO<Organization> {

    public OrganizationDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public @NotNull Optional<Organization> get(long id) {
        try (var statement = connection.prepareStatement(
                "SELECT organization_id, inn, name, account_number FROM organization WHERE organization_id = ?"
        )) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Organization.builder()
                            .organizationId(resultSet.getLong("organization_id"))
                            .inn(resultSet.getString("inn"))
                            .name(resultSet.getString("name"))
                            .accountNumber(resultSet.getString("account_number"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Organization save(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(
                "INSERT INTO organization(inn, name, account_number) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, entity.getInn());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getAccountNumber());
            statement.executeUpdate();

            try (var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    entity.setOrganizationId(resultSet.getLong("organization_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean update(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(
                "UPDATE organization SET inn = ?, name = ?, account_number = ? WHERE organization_id = ?"
        )) {
            statement.setString(1, entity.getInn());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getAccountNumber());
            statement.setLong(4, entity.getOrganizationId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(
                "DELETE FROM organization WHERE organization_id = ?"
        )) {
            statement.setLong(1, entity.getOrganizationId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
