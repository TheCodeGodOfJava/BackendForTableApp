package com.example.backEnd.queryDsl.builder;


import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.NUM;
import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.STR;
import static org.junit.jupiter.api.Assertions.*;

import com.example.backEnd.datatables.expression.ExpressionPredicateBuilder;
import com.example.backEnd.datatables.mapping.Column;
import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

public class ExpressionPredicateBuilderTest {

    private ExpressionPredicateBuilder epb;
    private DataTablesInput input;

    @BeforeEach
    public void init() {
        ExpressionTypeAlias eta = createExpressionTypeAlias();
        input = createDatatablesInput();
        epb = new ExpressionPredicateBuilder(eta, input);
    }

    @Test
    public void shouldBuildPredicate() {
        // when
        BooleanBuilder b = epb.build();

        // then
        assertNotNull(b);
        assertEquals("number field = 1245", b.toString());
        assertTrue(b.hasValue());
    }

    @Test
    public void shouldBuildPredicates() {
        // given

        Column column = input.getColumns().stream()
                .filter(c -> c.getAlias().equals("stringField"))
                .findFirst()
                .orElseThrow();
        column.setSearch("search string");

        // when
        BooleanBuilder b = epb.build();

        // then
        assertNotNull(b);
        assertEquals("number field = 1245 && string field like %search string%", b.toString());
        assertTrue(b.hasValue());
    }

    @Test
    public void shouldCreatePageable() {
        // when
        Pageable p = epb.createPageable();

        // then
        assertNotNull(p);
        assertNotNull(p.getSort());
        assertEquals(5, p.getPageSize());
        assertEquals(555, p.getOffset());
    }

    @Test
    public void shouldCreatePageableButMaximumLengthPage() {
        // given
        input.setLength(-1);

        // when
        Pageable p = epb.createPageable();

        // then
        assertNotNull(p);
        assertNotNull(p.getSort());
        assertEquals(Integer.MAX_VALUE, p.getPageSize());
        assertEquals(0, p.getOffset());
    }

    private ExpressionTypeAlias createExpressionTypeAlias() {
        var numberExpression = Expressions.numberTemplate(Long.class, "number field");
        var stringExpression = Expressions.stringTemplate("string field");
        return new ExpressionTypeAlias(
                Map.of("numberField", Pair.of(numberExpression, NUM), "stringField", Pair.of(stringExpression, STR)),
                ExpressionPredicateBuilderDto.class);
    }

    private DataTablesInput createDatatablesInput() {
        DataTablesInput dataTablesInput = new DataTablesInput();
        dataTablesInput.setLength(5);
        dataTablesInput.setStart(555);
        dataTablesInput.setColumns(List.of(
                Column.builder()
                        .alias("numberField")
                        .orderDirection("asc")
                        .search("1245")
                        .build(),
                Column.builder().alias("stringField").orderDirection("asc").build()));
        return dataTablesInput;
    }
}
