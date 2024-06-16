package com.example.backEnd.services;


import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.example.backEnd.datatables.filter.AbstractMasterService;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.models.QStudent;
import com.example.backEnd.models.Student;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.repositories.StudentRepository;
import com.querydsl.core.types.Expression;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StudentService
        extends AbstractMasterService<Student, StudentProjection> {

    private static final ExpressionTypeAlias table;

    static {
        Map<String, Pair<Expression<?>, ColumnValueType>> map = new HashMap<>();
        table = new ExpressionTypeAlias(map, StudentProjection.class);
    }


    public StudentService(EntityManager entityManager, StudentRepository studentRepository) {
        super(QStudent.student, entityManager, studentRepository, table);
    }
}
