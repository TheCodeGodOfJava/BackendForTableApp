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
import java.util.Map;

public class BaseFilterService<T> extends AbstractDataService {

  private final EntityPath<T> entityPath;
  private final EntityPath<?>[] entityJoins;
  private final Map<String, Pair<Expression<?>, MasterFilterType>> fieldPathMap = new HashMap<>();
  private final Map<String, ConstructorExpression<?>> fieldPathWithConstructorsMap =
      new HashMap<>();
  private final Map<String, StringPath> dependencyMap;

  public BaseFilterService(
      EntityPath<T> entityPath,
      EntityManager entityManager,
      Map<String, StringPath> dependencyMap,
      EntityPath<?>... entityJoins) {
    super(entityManager);
    this.entityPath = entityPath;
    this.dependencyMap = dependencyMap;
    this.entityJoins = entityJoins;
  }

  public Pair<?, Collection<?>> findByFieldAndTerm(
      String masterType,
      Long masterId,
      String field,
      String term,
      String dependencyAlias,
      String dependency,
      boolean tableToggle,
      Long pageSize,
      Long currentPage) {

    BooleanExpression exp = null;
    if (masterType != null && masterId != null && masterId != 0) {
      var masterFilterType =
          tableToggle ? MasterFilterType.NOT_EQUALS : fieldPathMap.get(field).getSecond();
      exp = getMasterExpression(masterType, masterId, masterFilterType);
    }
    return findByFieldAndTerm(field, term, dependencyAlias, dependency, pageSize, currentPage, exp);
  }

  public Pair<?, Collection<?>> findByFieldAndTerm(
      String field,
      String term,
      String dependencyAlias,
      String dependency,
      Long pageSize,
      Long currentPage,
      Predicate exp) {

    var fieldPath = fieldPathMap.get(field).getFirst();
    var finalQuery = getFinalQuery(field, fieldPath, term, exp, dependencyAlias, dependency, false);
    if (currentPage >= 0) {
      finalQuery = finalQuery.limit(pageSize).offset(currentPage * pageSize);
    }
    var countQuery = getFinalQuery(field, fieldPath, term, exp, dependencyAlias, dependency, true);
    return new Pair<>(countQuery.fetchOne(), finalQuery.fetch());
  }

  private JPAQuery<?> getFinalQuery(
      String field,
      Expression<?> fieldPath,
      String term,
      Predicate exp,
      String dependencyAlias,
      String dependency,
      boolean isCountQuery) {

    Expression<?> expression;
    if (isCountQuery) {
      expression = Expressions.numberTemplate(Long.class, "count(distinct {0})", fieldPath);
    } else {
      expression = getSelectExpression(field);
    }

    var query =
        new JPAQueryFactory(super.getEntityManager())
            .select(expression)
            .distinct()
            .from(entityPath);

    if (!isCountQuery) {
      query.orderBy(((ComparableExpressionBase<?>) fieldPath).asc());
    }

    for (var join : entityJoins) {
      query = query.join(join);
    }

    BooleanExpression whereExp =
        Expressions.stringTemplate("CAST({0} AS string)", fieldPath)
            .like("%" + term + "%")
            .and(exp);
    return query.where(addDependencyCase(whereExp, dependencyAlias, dependency));
  }

  private BooleanExpression addDependencyCase(
      BooleanExpression exp, String dependencyAlias, String dependency) {
    if (this.dependencyMap != null) {
      StringPath dependencyPath = this.dependencyMap.get(dependencyAlias);
      if (dependencyPath != null && dependency != null) {
        exp = exp.and(dependencyPath.eq(dependency));
      }
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
