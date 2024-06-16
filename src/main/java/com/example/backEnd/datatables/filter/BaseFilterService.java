package com.example.backEnd.datatables.filter;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class BaseFilterService<T> extends AbstractDataService {

    private final EntityPath<T> entityPath;
    private final StringPath dependencyPath;
    private final EntityPath<?>[] entityJoins;
    private final Map<String, Pair<Expression<?>, MasterFilterType>> fieldPathMap = new HashMap<>();
    private final Map<String, ConstructorExpression<?>> fieldPathWithConstructorsMap = new HashMap<>();

    public BaseFilterService(
            EntityPath<T> entityPath,
            EntityManager entityManager,
            StringPath dependencyPath,
            EntityPath<?>... entityJoins) {
        super(entityManager);
        this.entityPath = entityPath;
        this.dependencyPath = dependencyPath;
        this.entityJoins = entityJoins;
    }

    public Collection<?> findByFieldAndTerm(
            String masterType, Long masterId, String field, String term, String dependency) {

        BooleanExpression exp = null;
        if (masterType != null && masterId != null && masterId != 0) {
            var masterFilterType = fieldPathMap.get(field).getSecond();
            exp = getMasterExpression(masterType, masterId, masterFilterType);
        }
        return findByFieldAndTerm(field, term, dependency, exp);
    }

    public Collection<?> findByFieldAndTerm(String field, String term, String dependency, Predicate exp) {

        var fieldPath = fieldPathMap.get(field).getFirst();

        var query = new JPAQueryFactory(super.getEntityManager())
                .select(getSelectExpression(field))
                .distinct()
                .from(entityPath);
        for (var join : entityJoins) {
            query = query.join(join);
        }

        BooleanExpression whereExp = Expressions.stringTemplate("CAST({0} AS string)", fieldPath)
                .like("%" + term + "%")
                .and(exp);

        return query.where(addDependencyCase(whereExp, dependency)).fetch();
    }

    private BooleanExpression addDependencyCase(BooleanExpression exp, String dependency) {
        if (this.dependencyPath != null && dependency != null) {
            exp = exp.and(this.dependencyPath.eq(dependency));
        }
        return exp;
    }

    public void addFieldPath(String fieldName, Expression<?> fieldPath) {
        fieldPathMap.put(fieldName, Pair.of(fieldPath, MasterFilterType.EQUALS));
    }

    public void addFieldPath(String fieldName, Expression<?> fieldPath, MasterFilterType masterFilterType) {
        fieldPathMap.put(fieldName, Pair.of(fieldPath, masterFilterType));
    }

    public void addFieldPath(
            String fieldName, Expression<?> fieldPath, ConstructorExpression<?> constructorExpression) {
        addFieldPath(fieldName, fieldPath);
        if (constructorExpression != null) {
            fieldPathWithConstructorsMap.put(fieldName, constructorExpression);
        }
    }

    public void addFieldPath(
            String fieldName,
            Expression<?> fieldPath,
            MasterFilterType masterFilterType,
            ConstructorExpression<?> constructorExpression) {
        addFieldPath(fieldName, fieldPath, masterFilterType);
        if (constructorExpression != null) {
            fieldPathWithConstructorsMap.put(fieldName, constructorExpression);
        }
    }

    private Expression<?> getSelectExpression(String fieldName) {
        if (fieldPathWithConstructorsMap.containsKey(fieldName)) {
            return fieldPathWithConstructorsMap.get(fieldName);
        }
        return fieldPathMap.get(fieldName).getFirst();
    }
}
