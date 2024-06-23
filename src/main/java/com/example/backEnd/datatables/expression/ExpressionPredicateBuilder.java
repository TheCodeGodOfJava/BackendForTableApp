package com.example.backEnd.datatables.expression;

import com.example.backEnd.datatables.DataTablesPageRequest;
import com.example.backEnd.datatables.mapping.Column;
import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.StringUtils;

public record ExpressionPredicateBuilder(ExpressionTypeAlias expressionTypeAlias, DataTablesInput input) {

    public BooleanBuilder build() {
        var b = new BooleanBuilder();
        input.getColumns().stream()
                .filter(this::filterColumnWithoutSearch)
                .map(this::createColumnPredicate)
                .forEach(b::and);
        return b;
    }

    private Boolean filterColumnWithoutSearch(Column column) {
        return column != null && StringUtils.hasText(column.getSearch());
    }

    private Predicate createColumnPredicate(Column column) {
        var pair = expressionTypeAlias.getExpressionAndTypeByAlias(column.getAlias());
        return new ExpressionColumnFilter(column.getSearch()).createPredicate(pair);
    }

    public Pageable createPageable() {
        if (input.getLength() < 0) {
            input.setStart(0);
            input.setLength(Integer.MAX_VALUE);
        }
        var sort = getOrder().orElse(QSort.unsorted());
        return new DataTablesPageRequest(input.getStart(), input.getLength(), sort);
    }

    private Optional<QSort> getOrder() {
        return input.getColumns().stream()
                .filter(column -> column.getOrderDirection() != null)
                .findFirst()
                .map(column -> {
                    var pair = expressionTypeAlias.getAliasPathMap().get(column.getAlias());
                    var comparableExpressionBase = (ComparableExpressionBase<?>) pair.getFirst();

                    var descValue = Order.DESC.name().toLowerCase();
                    var orderSpecification = descValue.equals(column.getOrderDirection())
                            ? comparableExpressionBase.desc()
                            : comparableExpressionBase.asc();

                    return QSort.by(orderSpecification);
                });
    }
}
