package com.example.backEnd.datatables.filter;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

@Getter
public abstract class AbstractDataService {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final Map<String, Map<MasterFilterType, Function<Long, BooleanExpression>>> masterMap = new HashMap<>();

    protected AbstractDataService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public void addMaster(
            String masterType, MasterFilterType masterFilterType, Function<Long, BooleanExpression> function) {

        masterMap.putIfAbsent(masterType, new HashMap<>());
        masterMap.get(masterType).put(masterFilterType, function);
    }

    public BooleanExpression getMasterExpression(String masterType, Long masterId, MasterFilterType masterFilterType) {

        var masterFilterTypeMap = masterMap.get(masterType);
        if (masterFilterTypeMap == null || !masterFilterTypeMap.containsKey(masterFilterType)) {
            throw new IllegalStateException(String.format(
                    "Can`t find config for master type: `%s` and filter: `%s`", masterType, masterFilterType));
        }
        return masterFilterTypeMap.get(masterFilterType).apply(masterId);
    }
}
