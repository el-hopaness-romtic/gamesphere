package ru.gamesphere.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import ru.gamesphere.domain.tables.records.OrganizationRecord;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;
import static ru.gamesphere.domain.Tables.*;

@RequiredArgsConstructor
public class Report {

    private static final @NotNull TXTFormat FORMAT = TXTFormat.DEFAULT.maxColWidth(1000);
    private final @NotNull DSLContext context;

    public Report(Connection connection) {
        context = DSL.using(connection, SQLDialect.POSTGRES);
    }

    public static <R extends Record> void prettyPrint(@NotNull Result<R> result) {
        System.out.println(new String(
                result.format(FORMAT).getBytes(StandardCharsets.UTF_8)
        ));
    }

    public @NotNull Result<Record2<OrganizationRecord, BigDecimal>> top10ByProduct(long productId) {
        Field<BigDecimal> totalAmount = sum(POSITION.AMOUNT).as("total_amount");
        return context.select(ORGANIZATION, totalAmount)
                .from(ORGANIZATION)
                .innerJoin(SHIPPING_LIST).on(SHIPPING_LIST.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .innerJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .where(POSITION.PRODUCT_ID.eq(productId))
                .groupBy(ORGANIZATION.ORGANIZATION_ID)
                .orderBy(totalAmount.desc())
                .limit(10)
                .fetch();
    }

    public @NotNull Result<Record1<OrganizationRecord>> amountGreaterThanParameter(Map<Long, BigDecimal> productIdToAmounts) {
        var query = context.select(ORGANIZATION.ORGANIZATION_ID, POSITION.PRODUCT_ID)
                .from(ORGANIZATION)
                .innerJoin(SHIPPING_LIST).on(SHIPPING_LIST.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .innerJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .groupBy(ORGANIZATION.ORGANIZATION_ID, POSITION.PRODUCT_ID);

        Condition havingCondition = noCondition();
        int conditionCount = 0;
        for (Map.Entry<Long, BigDecimal> productIdToAmount : productIdToAmounts.entrySet()) {
            if (productIdToAmount.getValue().compareTo(BigDecimal.ZERO) > 0) {
                conditionCount++;
                havingCondition = havingCondition.or(
                        POSITION.PRODUCT_ID.eq(productIdToAmount.getKey()).and(
                                sum(POSITION.AMOUNT).greaterThan(productIdToAmount.getValue())
                        )
                );
            }
        }
        if (conditionCount == 0) {
            return context.select(ORGANIZATION).from(ORGANIZATION).fetch();
        }

        var x = query.having(havingCondition);
        return context.select(ORGANIZATION)
                .from(ORGANIZATION)
                .innerJoin(x).on(ORGANIZATION.ORGANIZATION_ID.eq(x.field(ORGANIZATION.ORGANIZATION_ID)))
                .groupBy(ORGANIZATION.ORGANIZATION_ID)
                .having(count(x.field(POSITION.PRODUCT_ID)).equal(conditionCount))
                .fetch();
    }

    public @NotNull Result<Record4<LocalDateTime, String, BigDecimal, BigDecimal>> amountGreaterThanParameter2(LocalDate start, LocalDate end) {
        return context.select(SHIPPING_LIST.SHIPPING_DATE, PRODUCT.NAME, sum(POSITION.AMOUNT), sum(POSITION.PRICE))
                .from(SHIPPING_LIST)
                .innerJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .innerJoin(PRODUCT).on(PRODUCT.PRODUCT_ID.eq(POSITION.PRODUCT_ID))
                .where(SHIPPING_LIST.SHIPPING_DATE.between(start.atStartOfDay(), end.atStartOfDay()))
                .groupBy(SHIPPING_LIST.SHIPPING_DATE, POSITION.PRODUCT_ID, PRODUCT.NAME)
                .fetch();
    }

    public @NotNull Result<Record2<String, BigDecimal>> productsAveragePrice(LocalDate start, LocalDate end) {
        return context.select(PRODUCT.NAME, round(sum(POSITION.PRICE).div(sum(POSITION.AMOUNT)), 4).as("avg"))
                .from(SHIPPING_LIST)
                .innerJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .innerJoin(PRODUCT).on(PRODUCT.PRODUCT_ID.eq(POSITION.PRODUCT_ID))
                .where(SHIPPING_LIST.SHIPPING_DATE.between(start.atStartOfDay(), end.atStartOfDay()))
                .groupBy(POSITION.PRODUCT_ID, PRODUCT.NAME)
                .fetch();
    }

    public @NotNull Map<String, Set<String>> organizationsProducts() {
        return context.select(ORGANIZATION.NAME, PRODUCT.NAME)
                .from(ORGANIZATION)
                .leftJoin(SHIPPING_LIST).on(SHIPPING_LIST.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .leftJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .leftJoin(PRODUCT).on(PRODUCT.PRODUCT_ID.eq(POSITION.PRODUCT_ID))
                .groupBy(ORGANIZATION.ORGANIZATION_ID, PRODUCT.PRODUCT_ID)
                .fetch()
                .collect(Collectors.groupingBy(
                        x -> x.get(ORGANIZATION.NAME),
                        Collectors.mapping(x -> {
                            String s = x.get(PRODUCT.NAME);
                            return s != null ? s : "";
                        }, Collectors.toSet())
                ));
    }
}
