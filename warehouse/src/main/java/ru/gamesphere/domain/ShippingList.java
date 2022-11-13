package ru.gamesphere.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ShippingList {
    private Long shippingListId;
    private LocalDate shippingDate;
    private Long organizationId;
}
