package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.backEnd.models.Student;
import com.example.backEnd.models.projections.StudentFormProjection;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.repositories.StudentRepository;
import com.example.backEnd.services.StudentService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        StudentFormProjection studentProjection = new StudentFormProjection();
        studentProjection.setId(1L);
        studentProjection.setFirstName("John");
        studentProjection.setLastName("Doe");
        studentProjection.setAge(25);

        Student studentEntity = new Student();
        studentEntity.setId(studentProjection.getId());
        studentEntity.setFirstName(studentProjection.getFirstName());
        studentEntity.setLastName(studentProjection.getLastName());
        studentEntity.setAge(studentProjection.getAge());

        // Mock behavior of ModelMapper
        when(modelMapper.map(studentProjection, Student.class)).thenReturn(studentEntity);
        when(modelMapper.map(studentEntity, StudentFormProjection.class)).thenReturn(studentProjection);

        // Mock behavior of StudentRepository
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        // When
        StudentProjection savedStudent = studentService.save(studentProjection);

        // Then
        assertEquals(studentProjection.getId(), savedStudent.getId());
        assertEquals(studentProjection.getFirstName(), savedStudent.getFirstName());
        assertEquals(studentProjection.getLastName(), savedStudent.getLastName());
        assertEquals(studentProjection.getAge(), savedStudent.getAge());

        // Verify that save method was called once with the correct entity
        verify(studentRepository, times(1)).save(studentEntity);
    }

    @Test
    public void testRemoveAll() {
        // Given
        List<Long> studentIds = Arrays.asList(1L, 2L, 3L);

        // When
        studentService.removeAll(studentIds);

        // Then
        // Verify that deleteAllById method was called once with the correct IDs
        verify(studentRepository, times(1)).deleteAllById(studentIds);
    }
}
