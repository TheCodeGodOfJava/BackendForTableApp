package controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backEnd.controllers.StudentController;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
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

    @Mock
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAll() throws Exception {
        // Stubbing the service method
        when(service.findAll(any(), any(), any(), any())).thenReturn(new DataTablesOutput<>());

        // Performing the mock MVC request
        mockMvc.perform(get("/students/all"))
                .andDo(print())
                .andExpect(status().isOk());

        // Verifying that the service method was called exactly once
        verify(service, times(1)).findAll(any(), any(), any(), any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void searchByTermAndField() throws Exception {
        String field = "name";
        String term = "John";

        // Stubbing the service method
        when(service.findByFieldAndTerm(any(), any(), eq(field), eq(term), any()))
                .thenReturn(Collections.emptyList());

        // Performing the mock MVC request
        mockMvc.perform(get("/students/filter")
                        .param("field", field)
                        .param("term", term))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Expecting an empty list

        // Verifying that the service method was called exactly once
        verify(service, times(1)).findByFieldAndTerm(any(), any(), eq(field), eq(term), any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void saveStudent() throws Exception {
        // Mock student projection to be saved
        var studentProjection = new StudentProjection();
        studentProjection.setId(1L);
        studentProjection.setFirstName("John Doe");

        // Mocking service method
        when(service.save(any(StudentProjection.class))).thenReturn(studentProjection);

        // Convert student projection to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String studentJson = objectMapper.writeValueAsString(studentProjection);

        // Perform POST request to save student
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/students/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson));

        // Verify HTTP status
        result.andExpect(status().isCreated());

        // Verify that service method was called once
        verify(service).save(any(StudentProjection.class));
    }


    @Test
    void removeStudents() throws Exception {
        // Mock IDs to be removed
        List<Long> idsToRemove = Arrays.asList(1L, 2L, 3L);

        // Mocking service method
        doNothing().when(service).removeAll(idsToRemove);

        // Convert IDs to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String idsJson = objectMapper.writeValueAsString(idsToRemove);

        // Perform DELETE request to remove students
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/students/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idsJson));

        // Verify HTTP status
        result.andExpect(status().isOk());

        // Verify that service method was called once with correct arguments
        verify(service).removeAll(idsToRemove);
    }
}
