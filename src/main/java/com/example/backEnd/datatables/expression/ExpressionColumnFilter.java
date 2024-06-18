package com.example.backEnd.datatables.expression;

import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValuePresence;
import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.*;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import org.springframework.data.util.Pair;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;

import java.util.HashMap;
import java.util.Map;

public class ExpressionColumnFilter {

    private final String[] filterValues;

    private static final Map<ColumnValuePresence, Map<ColumnValueType, IPredicate>> map;
    private static final Map<ColumnValueType, ColumnValuePresence> columnValuePresenceMap;

    public Map<ColumnValuePresence, Map<ColumnValueType, IPredicate>> getMap() {
        return map;
    }

    static {
        columnValuePresenceMap = new HashMap<>();
        columnValuePresenceMap.put(ColumnValueType.DATE, ColumnValuePresence.DATE_RANGE);
        columnValuePresenceMap.put(ColumnValueType.TIME, ColumnValuePresence.TIME_RANGE);
        columnValuePresenceMap.put(ColumnValueType.DATE_TIME, ColumnValuePresence.DATE_TIME_RANGE);

        var single = Map.of(
                ColumnValueType.STR,
                new StringSinglePredicate(),
                ColumnValueType.NUM,
                new NumberSinglePredicate(),
                ColumnValueType.BOOLEAN,
                new BooleanSinglePredicate());
        var multi = Map.of(
                ColumnValueType.STR,
                new StringMultiPredicate(),
                ColumnValueType.NUM,
                new NumberMultiPredicate(),
                ColumnValueType.BOOLEAN,
                new BooleanMultiPredicate());
        map = Map.of(
                ColumnValuePresence.SINGLE,
                single,
                ColumnValuePresence.MULTI,
                multi,
                ColumnValuePresence.DATE_RANGE,
                Map.of(ColumnValueType.DATE, new LocalDateRangePredicate()),
                ColumnValuePresence.TIME_RANGE,
                Map.of(ColumnValueType.TIME, new LocalTimeSinglePredicate()),
                ColumnValuePresence.DATE_TIME_RANGE,
                Map.of(ColumnValueType.DATE_TIME, new LocalDateTimeRangePredicate()));
    }

    public ExpressionColumnFilter(String filterValue) {
        filterValues = filterValue.split(",");
    }

    public Predicate createPredicate(Pair<Expression<?>, ColumnValueType> pair) {
        var expression = pair.getFirst();
        var type = pair.getSecond();

        ColumnValuePresence presence = columnValuePresenceMap.getOrDefault(
                type, filterValues.length == 1 ? ColumnValuePresence.SINGLE : ColumnValuePresence.MULTI);

        return getMap().get(presence).get(type).create(filterValues, expression);
    }
}
