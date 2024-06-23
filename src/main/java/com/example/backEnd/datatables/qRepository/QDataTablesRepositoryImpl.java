package com.example.backEnd.datatables.qRepository;


import com.example.backEnd.datatables.expression.ExpressionPredicateBuilder;
import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@Slf4j
public class QDataTablesRepositoryImpl<T, ID extends Serializable> extends CustomQueryDslJpaRepository<T, ID>
        implements QDataTablesRepository<T, ID> {

    public QDataTablesRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public <R> DataTablesOutput<R> findAllByProjection(
            ExpressionTypeAlias expressionTypeAlias,
            DataTablesInput input,
            Predicate additionalPredicate,
            EntityPath<?>... entityJoins) {

        DataTablesOutput<R> output = new DataTablesOutput<>();

        if (input.getLength() == 0) {
            input.setLength(-1);
        }
        if (count() == 0) {
            return output;
        }

        ExpressionPredicateBuilder predicateBuilder = new ExpressionPredicateBuilder(expressionTypeAlias, input);
        var predicate = predicateBuilder.build().and(additionalPredicate);

        FactoryExpression<R> factoryExpression =
                Projections.constructor(expressionTypeAlias.getProjection(), expressionTypeAlias.getExpressions());

        Page<R> data = findAll(factoryExpression, predicate, predicateBuilder.createPageable(), entityJoins);
        output.setData(data.getContent());
        output.setRecordsTotal(data.getTotalElements());
        return output;
    }
}
