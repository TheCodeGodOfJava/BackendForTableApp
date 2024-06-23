package com.example.backEnd.datatables.mapping;

import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.querydsl.core.types.Expression;
import jakarta.el.PropertyNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.util.Pair;

public class ExpressionTypeAlias {

    private final Class<?> projection;
    private final Set<String> fieldNames;
    @Getter
    Map<String, Pair<Expression<?>, ColumnValueType>> aliasPathMap;

    public ExpressionTypeAlias(Map<String, Pair<Expression<?>, ColumnValueType>> aliasPathMap, Class<?> projection) {

        this.aliasPathMap = aliasPathMap;
        this.projection = projection;

        this.fieldNames = getAllFields(new ArrayList<>(), projection).stream()
                .map(Field::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> unmappedFields = getUnmappedFields();
        if (!unmappedFields.isEmpty()) {
            throw new IllegalStateException(
                    "Projection and expressions have mismatched fields: " + String.join(", ", unmappedFields));
        }
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        Optional.ofNullable(type.getSuperclass()).ifPresent(s -> getAllFields(fields, type.getSuperclass()));
        return fields;
    }

    private Set<String> getUnmappedFields() {
        Set<String> aliases = aliasPathMap.keySet();
        return aliases.stream().filter(alias -> !fieldNames.contains(alias)).collect(Collectors.toSet());
    }

    public Pair<Expression<?>, ColumnValueType> getExpressionAndTypeByAlias(String alias) {
        return Optional.ofNullable(aliasPathMap.get(alias))
                .orElseThrow(() -> new PropertyNotFoundException(String.format("Alias: '%s' not found!", alias)));
    }

    public Expression<?>[] getExpressions() {
        return fieldNames.stream()
                .map(aliasPathMap::get)
                .filter(Objects::nonNull)
                .map(Pair::getFirst)
                .toArray(Expression<?>[]::new);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getProjection() {
        return (Class<T>) projection;
    }
}
