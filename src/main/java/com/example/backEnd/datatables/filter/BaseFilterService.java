package com.example.backEnd.datatables.filter;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseFilterService<T> extends AbstractDataService {

  private final EntityPath<T> entityPath;
  private final EntityPath<?>[] entityJoins;
  private final Map<String, Pair<Expression<?>, MasterFilterType>> fieldPathMap = new HashMap<>();
  private final Map<String, ConstructorExpression<?>> fieldPathWithConstructorsMap =
      new HashMap<>();

  public BaseFilterService(
      EntityPath<T> entityPath, EntityManager entityManager, EntityPath<?>... entityJoins) {
    super(entityManager);
    this.entityPath = entityPath;
    this.entityJoins = entityJoins;
  }

  public Pair<?, Collection<?>> findByFieldAndTerm(
      String masterType,
      Long masterId,
      String field,
      String term,
      Map<String, String> dependencies,
      boolean tableToggle,
      Long pageSize,
      Long currentPage) {

    BooleanExpression exp = null;
    if (masterType != null && masterId != null && masterId != 0) {
      var masterFilterType =
          tableToggle ? MasterFilterType.NOT_EQUALS : fieldPathMap.get(field).getSecond();
      exp = getMasterExpression(masterType, masterId, masterFilterType);
    }
    return findByFieldAndTerm(field, term, dependencies, pageSize, currentPage, exp);
  }

  public List<?> getParentSelectValue(String parentField, String childField, String childValue) {
    var parentFieldPath = fieldPathMap.get(parentField).getFirst();

    var query =
        new JPAQueryFactory(super.getEntityManager())
            .select(parentFieldPath)
            .distinct()
            .from(entityPath)
            .where(Expressions.stringPath(childField).eq(childValue));

    return query.fetch();
  }

  public Pair<?, Collection<?>> findByFieldAndTerm(
      String field,
      String term,
      Map<String, String> dependencies,
      Long pageSize,
      Long currentPage,
      Predicate exp) {

    var fieldPath = fieldPathMap.get(field).getFirst();
    var finalQuery = getFinalQuery(field, fieldPath, term, exp, dependencies, false);
    if (currentPage >= 0) {
      finalQuery = finalQuery.limit(pageSize).offset(currentPage * pageSize);
    }
    var countQuery = getFinalQuery(field, fieldPath, term, exp, dependencies, true);
    return new Pair<>(countQuery.fetchOne(), finalQuery.fetch());
  }

  private JPAQuery<?> getFinalQuery(
      String field,
      Expression<?> fieldPath,
      String term,
      Predicate exp,
      Map<String, String> dependencies,
      boolean isCountQuery) {

    Expression<?> expression = getQueryExpression(field, fieldPath, isCountQuery);
    JPAQuery<?> query = initializeQuery(expression, fieldPath, isCountQuery);

    BooleanExpression whereExp = buildWhereExpression(fieldPath, term, exp);
    return query.where(addDependencyCase(whereExp, dependencies));
  }

  private Expression<?> getQueryExpression(
      String field, Expression<?> fieldPath, boolean isCountQuery) {
    return isCountQuery
        ? Expressions.numberTemplate(Long.class, "count(distinct {0})", fieldPath)
        : getSelectExpression(field);
  }

  private JPAQuery<?> initializeQuery(
      Expression<?> expression, Expression<?> fieldPath, boolean isCountQuery) {
    var query =
        new JPAQueryFactory(super.getEntityManager())
            .select(expression)
            .distinct()
            .from(entityPath);

    for (var join : entityJoins) {
      query.join(join);
    }

    if (!isCountQuery) {
      query.orderBy(((ComparableExpressionBase<?>) fieldPath).asc());
    }

    return query;
  }

  private BooleanExpression buildWhereExpression(
      Expression<?> fieldPath, String term, Predicate exp) {
    return Expressions.stringTemplate("CAST({0} AS string)", fieldPath)
        .like("%" + term + "%")
        .and(exp);
  }

  private BooleanExpression addDependencyCase(
      BooleanExpression exp, Map<String, String> dependencies) {
    for (Map.Entry<String, String> entry : dependencies.entrySet()) {
      StringPath dependencyPath = Expressions.stringPath(entry.getKey());
      exp = exp.and(dependencyPath.eq(entry.getValue()));
    }
    return exp;
  }

  public void addFieldPath(String fieldName, Expression<?> fieldPath) {
    fieldPathMap.put(fieldName, Pair.of(fieldPath, MasterFilterType.EQUALS));
  }

  public void addFieldPath(
      String fieldName, Expression<?> fieldPath, MasterFilterType masterFilterType) {
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
