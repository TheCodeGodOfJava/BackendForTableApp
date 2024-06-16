package com.example.backEnd.queryDsl.find;

import com.example.backEnd.datatables.find.BaseFindService;
import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.datatables.qRepository.QDataTablesRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BaseFindServiceTest {

    @Mock
    private QDataTablesRepository<Object, Long> repository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ExpressionTypeAlias table;

    @InjectMocks
    private BaseFindService<Object, Object> baseFindService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        baseFindService = new BaseFindService<>(repository, table, entityManager);
    }

    @Test
    public void testFindAllWithExpression() {
        DataTablesInput input = new DataTablesInput();
        BooleanExpression exp = null;
        EntityPath<?>[] entityJoins = new EntityPath<?>[]{};

        DataTablesOutput<Object> expectedOutput = new DataTablesOutput<>();
        when(repository.findAllByProjection(any(), any(), any(), any(EntityPath[].class)))
                .thenReturn(expectedOutput);

        DataTablesOutput<Object> result = baseFindService.findAll(input, exp, entityJoins);

        assertNotNull(result);
    }

    @Test
    public void testFindAllWithMasterFilterAndNullExpression() {
        String masterType = null;
        Long masterId = null;
        Boolean tableToggle = false;
        DataTablesInput input = new DataTablesInput();
        EntityPath<?>[] entityJoins = new EntityPath<?>[]{};

        DataTablesOutput<Object> expectedOutput = new DataTablesOutput<>();
        when(repository.findAllByProjection(any(), any(), any(), any(EntityPath[].class)))
                .thenReturn(expectedOutput);

        DataTablesOutput<Object> result = baseFindService.findAll(masterType, masterId, tableToggle, input, entityJoins);

        assertNotNull(result);
    }
}
