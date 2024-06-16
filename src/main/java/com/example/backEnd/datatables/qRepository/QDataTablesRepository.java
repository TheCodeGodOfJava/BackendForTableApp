package com.example.backEnd.datatables.qRepository;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface QDataTablesRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, QuerydslPredicateExecutor<T> {

    <R> DataTablesOutput<R> findAllByProjection(
            ExpressionTypeAlias expressionTypeAlias,
            DataTablesInput input,
            Predicate additionalPredicate,
            EntityPath<?>... entityJoins);
}
