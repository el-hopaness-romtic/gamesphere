package ru.gamesphere.service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.gamesphere.domain.tables.records.OrganizationRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.jooq.impl.DSL.sum;
import static ru.gamesphere.domain.Tables.*;

@RequiredArgsConstructor
public class Report {

    private final Connection connection;

    List<OrganizationRecord> top10ByProduct(long productId) {
        DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

        Field<BigDecimal> totalAmount = sum(POSITION.AMOUNT).as("total_amount");
        return context.select(ORGANIZATION, totalAmount)
                .from(ORGANIZATION)
                .innerJoin(SHIPPING_LIST).on(SHIPPING_LIST.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .innerJoin(POSITION).on(POSITION.SHIPPING_LIST_ID.eq(SHIPPING_LIST.SHIPPING_LIST_ID))
                .where(POSITION.PRODUCT_ID.eq(productId))
                .groupBy(ORGANIZATION.ORGANIZATION_ID)
                .orderBy(totalAmount.desc())
                .limit(10)
                .fetchInto(ORGANIZATION);
    }

}
