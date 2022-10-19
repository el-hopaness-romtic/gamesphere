package ru.gamesphere.model;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class Author {
    @NotNull
    private Long authorId;
    @NotNull
    private String firstname;
    @Nullable
    private String surname;
}
