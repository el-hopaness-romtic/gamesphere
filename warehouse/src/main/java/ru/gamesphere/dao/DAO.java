package ru.gamesphere.dao;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class DAO<T> {
    protected final @NotNull Connection connection;

    abstract @NotNull Optional<T> get(long id);

    abstract @NotNull T save(@NotNull T entity);

    abstract boolean update(@NotNull T entity);

    abstract boolean delete(@NotNull T entity);
}
