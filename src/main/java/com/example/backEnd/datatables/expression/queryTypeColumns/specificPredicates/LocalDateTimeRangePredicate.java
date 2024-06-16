package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeRangePredicate implements IPredicate {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public Predicate create(String[] value, Expression<?> expression) {
        String[] dateValues = value[0].split("#");

        String localDateStringStart = dateValues[0].split("T")[0];
        String localDateStringEnd = dateValues[1].split("T")[0];

        var dateTimeFrom = String.format("%s 00:00", localDateStringStart);
        var dateTimeTo = String.format("%s 23:59", localDateStringEnd);

        return Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", expression)
                .between(
                        LocalDateTime.parse(dateTimeFrom, dateTimeFormatter),
                        LocalDateTime.parse(dateTimeTo, dateTimeFormatter));
    }
}
