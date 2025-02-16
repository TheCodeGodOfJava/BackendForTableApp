package com.example.backEnd.datatables.filter;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractMasterFilterService<T> {

  private final BaseFilterService<T> filterService;

  public AbstractMasterFilterService(
      EntityPath<T> entityPath,
      EntityManager entityManager,
      EntityPath<?>... entityJoins) {
    this.filterService =
        new BaseFilterService<>(entityPath, entityManager, entityJoins);
  }

  public void addMaster(
      String masterType,
      MasterFilterType masterFilterType,
      Function<Long, BooleanExpression> function) {
    filterService.addMaster(masterType, masterFilterType, function);
  }

  public void addFieldPath(String fieldName, Expression<?> fieldPath) {
    this.filterService.addFieldPath(fieldName, fieldPath);
  }

  public void addFieldPath(
      String fieldName, Expression<?> fieldPath, MasterFilterType masterFilterType) {
    this.filterService.addFieldPath(fieldName, fieldPath, masterFilterType);
  }

  public void addFieldPath(
      String fieldName, Expression<?> fieldPath, ConstructorExpression<?> constructorExpression) {
    this.filterService.addFieldPath(fieldName, fieldPath, constructorExpression);
  }

  public void addFieldPath(
      String fieldName,
      Expression<?> fieldPath,
      MasterFilterType masterFilterType,
      ConstructorExpression<?> constructorExpression) {

    this.filterService.addFieldPath(fieldName, fieldPath, masterFilterType, constructorExpression);
  }

  public Pair<?, Collection<?>> findByFieldAndTerm(
      Long masterId,
      String masterType,
      String field,
      String term,
      Map<String, String> dependencies,
      boolean tableToggle,
      Long pageSize,
      Long currentPage) {
    return filterService.findByFieldAndTerm(
        masterType, masterId, field, term, dependencies, tableToggle, pageSize, currentPage);
  }

  public List<?> getParentSelectValue(String parentField, String childField, String childValue) {
    return filterService.getParentSelectValue(parentField, childField, childValue);
  }
}
