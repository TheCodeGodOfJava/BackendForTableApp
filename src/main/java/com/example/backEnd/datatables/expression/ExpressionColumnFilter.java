package com.example.backEnd.datatables.expression;

import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValuePresence;
import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.*;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.util.Pair;

public class ExpressionColumnFilter {

    private static final Map<ColumnValuePresence, Map<ColumnValueType, IPredicate>> map;
    private static final Map<ColumnValueType, ColumnValuePresence> columnValuePresenceMap;

    static {
        columnValuePresenceMap = new HashMap<>();
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
                new BooleanSinglePredicate());
        map = Map.of(
                ColumnValuePresence.SINGLE,
                single,
                ColumnValuePresence.MULTI,
                multi,
                ColumnValuePresence.TIME_RANGE,
                Map.of(ColumnValueType.TIME, new LocalTimeSinglePredicate()),
                ColumnValuePresence.DATE_TIME_RANGE,
                Map.of(ColumnValueType.DATE_TIME, new LocalDateRangePredicate()));
    }

    private final String[] filterValues;

    public ExpressionColumnFilter(String filterValue) {
        boolean isObject = filterValue.charAt(0) == '{' && filterValue.charAt(filterValue.length() - 1) == '}';
        filterValues = isObject ? new String[]{filterValue} : filterValue.split(",");
    }

    public Map<ColumnValuePresence, Map<ColumnValueType, IPredicate>> getMap() {
        return map;
    }

    public Predicate createPredicate(Pair<Expression<?>, ColumnValueType> pair) {
        var expression = pair.getFirst();
        var type = pair.getSecond();

        ColumnValuePresence presence = columnValuePresenceMap.getOrDefault(
                type, filterValues.length == 1 ? ColumnValuePresence.SINGLE : ColumnValuePresence.MULTI);

        return getMap().get(presence).get(type).create(filterValues, expression);
    }
}
