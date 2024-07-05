package com.example.backEnd.services;

import static com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType.*;

import com.example.backEnd.datatables.expression.queryTypeColumns.ColumnValueType;
import com.example.backEnd.datatables.filter.AbstractMasterService;
import com.example.backEnd.datatables.mapping.ExpressionTypeAlias;
import com.example.backEnd.models.Professor;
import com.example.backEnd.models.QProfessor;
import com.example.backEnd.models.projections.ProfessorProjection;
import com.example.backEnd.repositories.ProfessorRepository;
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
public class ProfessorService extends AbstractMasterService<Professor, ProfessorProjection> {

  private static final ExpressionTypeAlias table;
  private static final QProfessor qProfessor = QProfessor.professor;
  private static final Map<String, StringPath> dependencyMap = new HashMap<>();

  static {
    Map<String, Pair<Expression<?>, ColumnValueType>> map = new HashMap<>();
    map.put("id", Pair.of(qProfessor.id, NUM));
    map.put("firstName", Pair.of(qProfessor.firstName, STR));
    map.put("lastName", Pair.of(qProfessor.lastName, STR));
    map.put("subject", Pair.of(qProfessor.subject, STR));
    map.put("phone", Pair.of(qProfessor.phone, STR));

    table = new ExpressionTypeAlias(map, ProfessorProjection.class);
  }

  private final ProfessorRepository professorRepository;
  private final ModelMapper modelMapper;

  public ProfessorService(
      EntityManager entityManager,
      ProfessorRepository professorRepository,
      ModelMapper modelMapper) {
    super(qProfessor, entityManager, professorRepository, table, dependencyMap);
    this.professorRepository = professorRepository;
    addFieldPath("id", qProfessor.id);
    addFieldPath("firstName", qProfessor.firstName);
    addFieldPath("lastName", qProfessor.lastName);
    addFieldPath("subject", qProfessor.subject);
    addFieldPath("phone", qProfessor.phone);
    this.modelMapper = modelMapper;
  }

  @Transactional
  public ProfessorProjection findById(Long id) {
    var professor =
        professorRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("No %s found with id: %s", "Professor", id)));
    return modelMapper.map(professor, ProfessorProjection.class);
  }

  @Transactional
  public ProfessorProjection save(ProfessorProjection professorProjection) {
    var professor = modelMapper.map(professorProjection, Professor.class);
    professor = this.professorRepository.save(professor);
    return this.modelMapper.map(professor, ProfessorProjection.class);
  }

  @Transactional
  public void removeAll(List<Long> ids) {
    professorRepository.deleteAllById(ids);
  }
}
