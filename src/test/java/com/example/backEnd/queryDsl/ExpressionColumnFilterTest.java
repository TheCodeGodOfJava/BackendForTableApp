package com.example.backEnd.queryDsl;

import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValuePresence.*;
import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.*;
import static org.mockito.Mockito.*;

import com.example.backEnd.datatables.expression.ExpressionColumnFilter;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.NumberSinglePredicate;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.StringMultiPredicate;
import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.StringSinglePredicate;
import com.querydsl.core.types.dsl.Expressions;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

@ExtendWith(MockitoExtension.class)
public class ExpressionColumnFilterTest {

    @Test
    public void shouldCallTypeStringSingleValuePredicate() {
        // given
        var strSingle = Mockito.mock(StringSinglePredicate.class);

        var expressionColumnFilter = Mockito.spy(new ExpressionColumnFilter("value"));
        var stringExpression = Expressions.stringTemplate("");

        // when
        when(expressionColumnFilter.getMap()).thenReturn(Map.of(SINGLE, Map.of(STR, strSingle)));

        // then
        expressionColumnFilter.createPredicate(Pair.of(stringExpression, STR));

        verify(strSingle, times(1)).create(any(), any());
        verifyNoMoreInteractions(strSingle);
    }

    @Test
    public void shouldCallTypeStringMultiValuesPredicate() {
        // given
        var strMulti = Mockito.mock(StringMultiPredicate.class);

        var expressionColumnFilter = Mockito.spy(new ExpressionColumnFilter("valueOne,valueTwo,valueThree"));
        var stringExpression = Expressions.stringTemplate("");

        // when
        when(expressionColumnFilter.getMap()).thenReturn(Map.of(MULTI, Map.of(STR, strMulti)));

        // then
        expressionColumnFilter.createPredicate(Pair.of(stringExpression, STR));

        verify(strMulti, times(1)).create(any(), any());
        verifyNoMoreInteractions(strMulti);
    }

    @Test
    public void shouldCallTypeNumberSinglePredicate() {
        // given
        var numberSingle = Mockito.mock(NumberSinglePredicate.class);

        var expressionColumnFilter = Mockito.spy(new ExpressionColumnFilter("12"));
        var numberExpression = Expressions.numberTemplate(Long.class, "");

        // when
        when(expressionColumnFilter.getMap()).thenReturn(Map.of(SINGLE, Map.of(NUM, numberSingle)));

        // then
        expressionColumnFilter.createPredicate(Pair.of(numberExpression, NUM));

        verify(numberSingle, times(1)).create(any(), any());
        verifyNoMoreInteractions(numberSingle);
    }

    @Test
    public void shouldCallTypeNumberMultiPredicate() {
        // given
        var numberMulti = Mockito.mock(NumberSinglePredicate.class);

        var expressionColumnFilter = Mockito.spy(new ExpressionColumnFilter("12,25,181"));
        var numberExpression = Expressions.numberTemplate(Long.class, "");

        // when
        when(expressionColumnFilter.getMap()).thenReturn(Map.of(MULTI, Map.of(NUM, numberMulti)));

        // then
        expressionColumnFilter.createPredicate(Pair.of(numberExpression, NUM));

        verify(numberMulti, times(1)).create(any(), any());
        verifyNoMoreInteractions(numberMulti);
    }
}
