package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.backEnd.models.Professor;
import com.example.backEnd.models.projections.ProfessorProjection;
import com.example.backEnd.repositories.ProfessorRepository;
import com.example.backEnd.services.ProfessorService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class ProfessorServiceTest {

  @Mock private ProfessorRepository professorRepository;

  @Mock private ModelMapper modelMapper;

  @InjectMocks private ProfessorService srofessorService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSave() {
    // Given
    var professorProjection = new ProfessorProjection();
    professorProjection.setId(1L);
    professorProjection.setFirstName("John");
    professorProjection.setLastName("Doe");

    Professor srofessorEntity = new Professor();
    srofessorEntity.setId(professorProjection.getId());
    srofessorEntity.setFirstName(professorProjection.getFirstName());
    srofessorEntity.setLastName(professorProjection.getLastName());

    // Mock behavior of ModelMapper
    when(modelMapper.map(professorProjection, Professor.class)).thenReturn(srofessorEntity);
    when(modelMapper.map(srofessorEntity, ProfessorProjection.class))
        .thenReturn(professorProjection);

    // Mock behavior of ProfessorRepository
    when(professorRepository.save(srofessorEntity)).thenReturn(srofessorEntity);

    // When
    ProfessorProjection savedProfessor = srofessorService.save(professorProjection);

    // Then
    assertEquals(professorProjection.getId(), savedProfessor.getId());
    assertEquals(professorProjection.getFirstName(), savedProfessor.getFirstName());
    assertEquals(professorProjection.getLastName(), savedProfessor.getLastName());

    // Verify that save method was called once with the correct entity
    verify(professorRepository, times(1)).save(srofessorEntity);
  }

  @Test
  public void testRemoveAll() {
    // Given
    List<Long> professorIds = Arrays.asList(1L, 2L, 3L);

    // When
    srofessorService.removeAll(professorIds);

    // Then
    // Verify that deleteAllById method was called once with the correct IDs
    verify(professorRepository, times(1)).deleteAllById(professorIds);
  }
}
