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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.*;

@Slf4j
@Service
public class StudentService
        extends AbstractMasterService<Student, StudentProjection> {

    private static final ExpressionTypeAlias table;
    private static final QStudent qStudent = QStudent.student;
    private final StudentRepository studentRepository;

    static {
        Map<String, Pair<Expression<?>, ColumnValueType>> map = new HashMap<>();
        map.put("id", Pair.of(qStudent.id, NUM));
        map.put("firstName", Pair.of(qStudent.firstName, STR));
        map.put("lastName", Pair.of(qStudent.lastName, STR));
        map.put("age", Pair.of(qStudent.age, NUM));
        map.put("gender", Pair.of(qStudent.gender, BOOLEAN));
        map.put("enrollDate", Pair.of(qStudent.enrollDate, DATE_TIME));
        map.put("about", Pair.of(qStudent.about, STR));
        table = new ExpressionTypeAlias(map, StudentProjection.class);
    }

    private final ModelMapper modelMapper;


    public StudentService(EntityManager entityManager, StudentRepository studentRepository, ModelMapper modelMapper) {
        super(qStudent, entityManager, studentRepository, table);
        this.studentRepository = studentRepository;
        addFieldPath("id", qStudent.id);
        addFieldPath("firstName", qStudent.firstName);
        addFieldPath("lastName", qStudent.lastName);
        addFieldPath("age", qStudent.age);
        this.modelMapper = modelMapper;
    }

    @Transactional
    public StudentProjection save(StudentProjection studentProjection) {
        var student = modelMapper.map(studentProjection, Student.class);
        student = this.studentRepository.save(student);
        return this.modelMapper.map(student, StudentProjection.class);
    }

    @Transactional
    public void removeAll(List<Long> ids) {
        studentRepository.deleteAllById(ids);
    }
}
