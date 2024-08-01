package controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backEnd.controllers.StudentController;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.StudentFormProjection;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

  @Mock private StudentService service;

  @InjectMocks private StudentController controller;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void getAll() throws Exception {
    // Stubbing the service method
    DataTablesOutput<StudentProjection> output = new DataTablesOutput<>();
    StudentProjection firstStudent = new StudentProjection();
    firstStudent.setId(1L);
    firstStudent.setFirstName("John");
    firstStudent.setLastName("Doe");
    StudentProjection secondStudent = new StudentProjection();
    secondStudent.setId(2L);
    secondStudent.setFirstName("Jane");
    secondStudent.setLastName("Smith");
    output.setData(Arrays.asList(firstStudent, secondStudent));
    when(service.findAll(any(), any(), any(), any())).thenReturn(output);

    // Performing the mock MVC request
    mockMvc
        .perform(
            get("/students/all")
                .param("masterId", "1")
                .param("masterType", "type")
                .param("tableToggle", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].id").value(firstStudent.getId()))
        .andExpect(jsonPath("$.data[0].firstName").value(firstStudent.getFirstName()))
        .andExpect(jsonPath("$.data[0].lastName").value(firstStudent.getLastName()))
        .andExpect(jsonPath("$.data[1].id").value(secondStudent.getId()))
        .andExpect(jsonPath("$.data[1].firstName").value(secondStudent.getFirstName()))
        .andExpect(jsonPath("$.data[1].lastName").value(secondStudent.getLastName()));

    // Verifying that the service method was called exactly once
    verify(service, times(1)).findAll(any(), any(), any(), any());
    verifyNoMoreInteractions(service);
  }

  @Test
  void searchByTermAndField() throws Exception {
    String field = "name";
    String term = "John";

    // Stubbing the service method
    when(service.findByFieldAndTerm(any(), any(), eq(field), eq(term), any(), any(), eq(false)))
        .thenReturn(Collections.emptyList());

    // Performing the mock MVC request
    mockMvc
        .perform(get("/students/filter").param("field", field).param("term", term))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0))); // Expecting an empty list

    // Verifying that the service method was called exactly once
    verify(service, times(1)).findByFieldAndTerm(any(), any(), eq(field), eq(term), any(), any(), eq(false));
    verifyNoMoreInteractions(service);
  }

  @Test
  void save() throws Exception {
    // Mock student projection to be saved
    var studentProjection = new StudentFormProjection();
    studentProjection.setId(1L);
    studentProjection.setFirstName("John Doe");

    // Mocking service method
    when(service.save(any(StudentFormProjection.class))).thenReturn(studentProjection);

    // Convert student projection to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String studentJson = objectMapper.writeValueAsString(studentProjection);

    // Perform POST request to save student
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/students/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson));

    // Verify HTTP status
    result.andExpect(status().isCreated());

    // Verify that service method was called once
    verify(service).save(any(StudentFormProjection.class));
  }

  @Test
  void remove() throws Exception {
    // Mock IDs to be removed
    List<Long> idsToRemove = Arrays.asList(1L, 2L, 3L);

    // Mocking service method
    doNothing().when(service).removeAll(idsToRemove);

    // Convert IDs to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String idsJson = objectMapper.writeValueAsString(idsToRemove);

    // Perform DELETE request to remove students
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/students/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idsJson));

    // Verify HTTP status
    result.andExpect(status().isOk());

    // Verify that service method was called once with correct arguments
    verify(service).removeAll(idsToRemove);
  }

  @Test
  public void getById() throws Exception {
    // Mock student ID
    Long studentId = 1L;

    // Mock student projection
    var studentProjection = new StudentFormProjection();
    studentProjection.setId(studentId);
    studentProjection.setFirstName("John");

    // Stubbing the service method
    when(service.findByIdProjection(studentId)).thenReturn(studentProjection);

    // Perform GET request to retrieve student by ID
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.get("/students/getOneById")
                .param("id", String.valueOf(studentId))
                .contentType(MediaType.APPLICATION_JSON));

    // Verify HTTP status and JSON response
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(studentProjection.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(studentProjection.getFirstName()));

    // Verify that service method was called once with correct ID argument
    verify(service).findByIdProjection(eq(studentId));
  }

  @Test
  void unbind() throws Exception {
    // Mock master ID and IDs to be unbound
    Long masterId = 1L;
    List<Long> idsToUnbind = Arrays.asList(2L, 3L);

    // Mocking service method
    doNothing().when(service).unbindAll(masterId, idsToUnbind);

    // Convert IDs to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String idsJson = objectMapper.writeValueAsString(idsToUnbind);

    // Perform DELETE request to unbind students
    ResultActions result =
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/students/unbind")
                            .param("masterId", String.valueOf(masterId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(idsJson));

    // Verify HTTP status
    result.andExpect(status().isOk());

    // Verify that service method was called once with correct arguments
    verify(service).unbindAll(masterId, idsToUnbind);
  }
}
