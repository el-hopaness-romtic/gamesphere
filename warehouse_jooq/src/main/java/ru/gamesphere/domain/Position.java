package ru.gamesphere.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Position {
    private Long positionId;
    private Long shippingListId;
    private Long productId;
    private BigDecimal price;
    private Integer amount;
}
