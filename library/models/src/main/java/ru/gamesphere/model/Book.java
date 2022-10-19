package ru.gamesphere.model;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class Book {
    @NotNull
    private Long bookId;
    @NotNull
    private String title;
    @Nullable
    private Short year;
}
