package com.example.backEnd.datatables.qRepository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CustomQueryDslJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements QuerydslPredicateExecutor<T> {
    private final QuerydslPredicateExecutor<T> querydslPredicateExecutor;
    private final Querydsl querydsl;

    public CustomQueryDslJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, SimpleEntityPathResolver.INSTANCE);
    }

    public CustomQueryDslJpaRepository(
            JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
        super(entityInformation, entityManager);
        this.querydslPredicateExecutor = new QuerydslPredicateExecutor<>(
                entityInformation, entityManager, SimpleEntityPathResolver.INSTANCE, null);
        EntityPath<T> path = resolver.createPath(entityInformation.getJavaType());
        PathBuilder<T> builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @NonNull
    public <P> Optional<P> findOne(@NonNull JPQLQuery<P> query) {
        return Optional.ofNullable(query.fetchFirst());
    }

    @NonNull
    public <P> List<P> findAll(@NonNull JPQLQuery<P> query) {
        return query.fetch();
    }

    @NonNull
    public <P> Page<P> findAll(
            @NonNull FactoryExpression<P> factoryExpression,
            @NonNull Predicate predicate,
            @NonNull Pageable pageable,
            EntityPath<?>... entityJoins) {

        JPQLQuery<P> query = createQuery(factoryExpression, predicate, entityJoins);
        JPQLQuery<?> countQuery = querydslPredicateExecutor.createCountQuery(predicate);
        return getPage(query, countQuery, pageable);
    }

    @Override
    @NonNull
    public Optional<T> findOne(@NonNull Predicate predicate) {
        return querydslPredicateExecutor.findOne(predicate);
    }

    @Override
    @NonNull
    public Iterable<T> findAll(@NonNull Predicate predicate) {
        return querydslPredicateExecutor.findAll(predicate);
    }

    @Override
    @NonNull
    public Iterable<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort) {
        return querydslPredicateExecutor.findAll(predicate, sort);
    }

    @Override
    @NonNull
    public Iterable<T> findAll(@NonNull Predicate predicate, @NonNull OrderSpecifier<?>... orders) {
        return querydslPredicateExecutor.findAll(predicate, orders);
    }

    @Override
    @NonNull
    public Iterable<T> findAll(@NonNull OrderSpecifier<?>... orders) {
        return querydslPredicateExecutor.findAll(orders);
    }

    @Override
    @NonNull
    public Page<T> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable) {
        return querydslPredicateExecutor.findAll(predicate, pageable);
    }

    @Override
    public long count(@NonNull Predicate predicate) {
        return querydslPredicateExecutor.count(predicate);
    }

    @Override
    public boolean exists(@NonNull Predicate predicate) {
        return querydslPredicateExecutor.exists(predicate);
    }

    @Override
    public <S extends T, R>  @NotNull R findBy(
            @NotNull Predicate predicate, @NotNull Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private <P> JPQLQuery<P> createQuery(
            FactoryExpression<P> factoryExpression, Predicate predicate, EntityPath<?>... entityJoins) {
        var query = querydslPredicateExecutor.createQuery(predicate).select(factoryExpression);

        for (var join : entityJoins) {
            query = query.join(join);
        }

        return query;
    }

    private <P> Page<P> getPage(JPQLQuery<P> query, JPQLQuery<?> countQuery, Pageable pageable) {
        JPQLQuery<P> paginatedQuery = querydsl.applyPagination(pageable, query);
        return PageableExecutionUtils.getPage(paginatedQuery.fetch(), pageable, countQuery::fetchCount);
    }

    private static class QuerydslPredicateExecutor<T> extends QuerydslJpaPredicateExecutor<T> {
        QuerydslPredicateExecutor(
                JpaEntityInformation<T, ?> entityInformation,
                EntityManager entityManager,
                EntityPathResolver resolver,
                CrudMethodMetadata metadata) {
            super(entityInformation, entityManager, resolver, metadata);
        }

        @Override
        @NonNull
        public JPQLQuery<?> createCountQuery(Predicate... predicate) {
            return super.createCountQuery(predicate);
        }

        @Override
        @NonNull
        public AbstractJPAQuery<?, ?> createQuery(Predicate @NotNull ... predicate) {
            return super.createQuery(predicate);
        }
    }
}
