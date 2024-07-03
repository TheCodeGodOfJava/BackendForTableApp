package com.example.backEnd.services;

import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.*;

import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.example.backEnd.datatables.filter.AbstractMasterService;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.models.QStudent;
import com.example.backEnd.models.Student;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.repositories.StudentRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService extends AbstractMasterService<Student, StudentProjection> {

  private static final ExpressionTypeAlias table;
  private static final QStudent qStudent = QStudent.student;
  private static final Map<String, StringPath> dependencyMap = new HashMap<>();

  static {
    Map<String, Pair<Expression<?>, ColumnValueType>> map = new HashMap<>();
    map.put("id", Pair.of(qStudent.id, NUM));
    map.put("firstName", Pair.of(qStudent.firstName, STR));
    map.put("lastName", Pair.of(qStudent.lastName, STR));
    map.put("age", Pair.of(qStudent.age, NUM));
    map.put("gender", Pair.of(qStudent.gender, BOOLEAN));
    map.put("enrollDate", Pair.of(qStudent.enrollDate, DATE_TIME));
    map.put("about", Pair.of(qStudent.about, STR));
    map.put("country", Pair.of(qStudent.country, STR));
    map.put("state", Pair.of(qStudent.state, STR));
    map.put("city", Pair.of(qStudent.city, STR));
    table = new ExpressionTypeAlias(map, StudentProjection.class);

    dependencyMap.put("state", qStudent.state);
    dependencyMap.put("country", qStudent.country);
  }

  private final StudentRepository studentRepository;
  private final ModelMapper modelMapper;

  public StudentService(
      EntityManager entityManager, StudentRepository studentRepository, ModelMapper modelMapper) {
    super(qStudent, entityManager, studentRepository, table, dependencyMap);
    this.studentRepository = studentRepository;
    addFieldPath("id", qStudent.id);
    addFieldPath("firstName", qStudent.firstName);
    addFieldPath("lastName", qStudent.lastName);
    addFieldPath("age", qStudent.age);
    addFieldPath("country", qStudent.country);
    addFieldPath("state", qStudent.state);
    addFieldPath("city", qStudent.city);
    this.modelMapper = modelMapper;
  }

  @Transactional
  public StudentProjection findById(Long id) {
    var customerWorkOrderNumber =
        studentRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("No %s found with id: %s", "Student", id)));

    return modelMapper.map(customerWorkOrderNumber, StudentProjection.class);
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
