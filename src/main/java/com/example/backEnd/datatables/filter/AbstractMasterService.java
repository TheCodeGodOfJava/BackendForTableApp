package com.example.backEnd.datatables.filter;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.datatables.qRepository.QDataTablesRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.persistence.EntityManager;

import java.util.function.Function;

public abstract class AbstractMasterService<T, P> extends AbstractMasterFilterService<T> {

    private final BaseFindService<T, P> findService;
    private final EntityPath<?>[] entityJoins;

    public AbstractMasterService(
            EntityPath<T> entityPath,
            EntityManager entityManager,
            QDataTablesRepository<T, Long> repository,
            ExpressionTypeAlias table,
            EntityPath<?>... entityJoins) {

        super(entityPath, entityManager);
        findService = new BaseFindService<>(repository, table, entityManager);
        this.entityJoins = entityJoins;
    }

    public AbstractMasterService(
            EntityPath<T> entityPath,
            EntityManager entityManager,
            QDataTablesRepository<T, Long> repository,
            ExpressionTypeAlias table,
            StringPath dependencyPath,
            EntityPath<?>... entityJoins) {

        super(entityPath, entityManager, dependencyPath);
        findService = new BaseFindService<>(repository, table, entityManager);
        this.entityJoins = entityJoins;
    }

    public DataTablesOutput<P> findAll(DataTablesInput input, BooleanExpression exp) {
        return findService.findAll(input, exp, entityJoins);
    }

    public DataTablesOutput<P> findAll(Long masterId, String masterType, Boolean tableToggle, DataTablesInput input) {
        return findService.findAll(masterType, masterId, tableToggle, input, entityJoins);
    }

    public void addMaster(
            String masterType, MasterFilterType masterFilterType, Function<Long, BooleanExpression> function) {
        super.addMaster(masterType, masterFilterType, function);
        findService.addMaster(masterType, masterFilterType, function);
    }
}
