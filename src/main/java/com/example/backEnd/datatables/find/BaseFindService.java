package com.example.backEnd.datatables.find;

import com.example.backEnd.datatables.filter.AbstractDataService;
import com.example.backEnd.datatables.filter.MasterFilterType;
import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.datatables.qRepository.QDataTablesRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;

public class BaseFindService<T, P> extends AbstractDataService {

    protected final QDataTablesRepository<T, Long> repository;
    protected final ExpressionTypeAlias table;

    public BaseFindService(
            QDataTablesRepository<T, Long> repository, ExpressionTypeAlias table, EntityManager entityManager) {
        super(entityManager);
        this.repository = repository;
        this.table = table;
    }

    public DataTablesOutput<P> findAll(DataTablesInput input, BooleanExpression exp, EntityPath<?>... entityJoins) {
        return repository.findAllByProjection(table, input, exp, entityJoins);
    }

    public DataTablesOutput<P> findAll(
            String masterType,
            Long masterId,
            Boolean tableToggle,
            DataTablesInput input,
            EntityPath<?>... entityJoins) {

        var masterFilterType = tableToggle ? MasterFilterType.NOT_EQUALS : MasterFilterType.EQUALS;
        BooleanExpression exp = (masterType != null && masterId != null && masterId != 0)
                ? getMasterExpression(masterType, masterId, masterFilterType)
                : null;
        return repository.findAllByProjection(table, input, exp, entityJoins);
    }
}
