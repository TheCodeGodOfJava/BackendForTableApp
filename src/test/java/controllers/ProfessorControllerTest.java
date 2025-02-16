package controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.backEnd.controllers.ProfessorController;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.ProfessorProjection;
import com.example.backEnd.services.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Pair;
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
class ProfessorControllerTest {

  @Mock private ProfessorService service;

  @InjectMocks private ProfessorController controller;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void getAll() throws Exception {
    // Stubbing the service method
    DataTablesOutput<ProfessorProjection> output = new DataTablesOutput<>();
    ProfessorProjection firstProfessor = new ProfessorProjection();
    firstProfessor.setId(1L);
    firstProfessor.setFirstName("John");
    firstProfessor.setLastName("Doe");
    ProfessorProjection secondProfessor = new ProfessorProjection();
    secondProfessor.setId(2L);
    secondProfessor.setFirstName("Jane");
    secondProfessor.setLastName("Smith");
    List<ProfessorProjection> professors = Arrays.asList(firstProfessor, secondProfessor);
    output.setData(professors);
    when(service.findAll(any(), any(), any(), any())).thenReturn(output);

    // Performing the mock MVC request
    mockMvc
        .perform(
            get("/professors/all")
                .param("masterId", "1")
                .param("masterType", "type")
                .param("tableToggle", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].id").value(firstProfessor.getId()))
        .andExpect(jsonPath("$.data[0].firstName").value(firstProfessor.getFirstName()))
        .andExpect(jsonPath("$.data[0].lastName").value(firstProfessor.getLastName()))
        .andExpect(jsonPath("$.data[1].id").value(secondProfessor.getId()))
        .andExpect(jsonPath("$.data[1].firstName").value(secondProfessor.getFirstName()))
        .andExpect(jsonPath("$.data[1].lastName").value(secondProfessor.getLastName()));

    // Verifying that the service method was called exactly once
    verify(service, times(1)).findAll(any(), any(), any(), any());
    verifyNoMoreInteractions(service);
  }

  @Test
  void searchByTermAndField() throws Exception {
    String field = "name";
    String term = "John";

    // Stubbing the service method
    doReturn(Pair.of(0L, Collections.emptyList()))
        .when(service)
        .findByFieldAndTerm(any(), any(), eq(field), eq(term), anyMap(), eq(false), any(), any());

    // Performing the mock MVC request
    mockMvc
        .perform(get("/professors/filter").param("field", field).param("term", term))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.second", hasSize(0)));

    // Verifying that the service method was called exactly once
    verify(service, times(1))
        .findByFieldAndTerm(any(), any(), eq(field), eq(term), anyMap(), eq(false), any(), any());
    verifyNoMoreInteractions(service);
  }

  @Test
  void getParentSelectValue_ReturnsExpectedList() throws Exception {

    String parentFieldAlias = "country";
    String childFieldAlias = "city";
    String childFieldValue = "New York";
    List<String> mockResponse = List.of("USA", "Canada");

    doReturn(mockResponse)
        .when(service)
        .getParentSelectValue(parentFieldAlias, childFieldAlias, childFieldValue);

    mockMvc
        .perform(
            get("/professors/getParentSelectValue")
                .param("parentFieldAlias", parentFieldAlias)
                .param("childFieldAlias", childFieldAlias)
                .param("childFieldValue", childFieldValue))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(mockResponse.size()))
        .andExpect(jsonPath("$[0]").value("USA"))
        .andExpect(jsonPath("$[1]").value("Canada"));

    // Verify the service method was called once
    verify(service, times(1))
        .getParentSelectValue(parentFieldAlias, childFieldAlias, childFieldValue);
  }

  @Test
  void save() throws Exception {
    // Mock professor projection to be saved
    var professorProjection = new ProfessorProjection();
    professorProjection.setId(1L);
    professorProjection.setFirstName("John Doe");

    // Mocking service method
    when(service.save(any(ProfessorProjection.class))).thenReturn(professorProjection);

    // Convert professor projection to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String professorJson = objectMapper.writeValueAsString(professorProjection);

    // Perform POST request to save professor
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/professors/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorJson));

    // Verify HTTP status
    result.andExpect(status().isCreated());

    // Verify that service method was called once
    verify(service).save(any(ProfessorProjection.class));
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

    // Perform DELETE request to remove professors
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/professors/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idsJson));

    // Verify HTTP status
    result.andExpect(status().isOk());

    // Verify that service method was called once with correct arguments
    verify(service).removeAll(idsToRemove);
  }

  @Test
  public void getById() throws Exception {
    // Mock professor ID
    Long professorId = 1L;

    // Mock professor projection
    var professorProjection = new ProfessorProjection();
    professorProjection.setId(professorId);
    professorProjection.setFirstName("John");

    // Stubbing the service method
    when(service.findByIdProjection(professorId)).thenReturn(professorProjection);

    // Perform GET request to retrieve professor by ID
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.get("/professors/getOneById")
                .param("id", String.valueOf(professorId))
                .contentType(MediaType.APPLICATION_JSON));

    // Verify HTTP status and JSON response
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(professorProjection.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(professorProjection.getFirstName()));

    // Verify that service method was called once with correct ID argument
    verify(service).findByIdProjection(eq(professorId));
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

    // Perform DELETE request to unbind professors
    ResultActions result =
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/professors/unbind")
                .param("masterId", String.valueOf(masterId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(idsJson));

    // Verify HTTP status
    result.andExpect(status().isOk());

    // Verify that service method was called once with correct arguments
    verify(service).unbindAll(masterId, idsToUnbind);
  }
}
