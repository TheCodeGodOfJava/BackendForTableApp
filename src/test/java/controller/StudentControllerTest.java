package controller;

import com.example.backEnd.controllers.StudentController;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
