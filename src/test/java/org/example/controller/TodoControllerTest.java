package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    private TodoEntity expected;

    @MockBean
    private TodoService todoService;

    @BeforeEach
    void setup() {
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setOrder(0L);
        this.expected.setTitle("Title");
        this.expected.setCompleted(false);
    }

    @Test
    void create() throws Exception {
        Mockito.when(this.todoService.add(ArgumentMatchers.any(TodoRequest.class))).then((i)-> {
            TodoRequest request = i.getArgument(0, TodoRequest.class);
            return  new TodoEntity(this.expected.getId(), request.getTitle(), this.expected.getOrder(), this.expected.getCompleted());
        });

        TodoRequest request = new TodoRequest();
        request.setTitle("AnY TITLE");
        ObjectMapper mapper = new ObjectMapper();

        String content = mapper.writeValueAsString(request);

        this.mvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("AnY TITLE"));

    }

    @Test
    void readOne() {
    }
}