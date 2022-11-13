package ru.gamesphere.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private long productId;
    private String name;
}
