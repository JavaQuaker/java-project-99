package hexlet.code.util.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import jdk.jshell.Snippet;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TasksStatusControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private ObjectMapper om;


    private TaskStatus testTaskStatus;
    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private Faker faker;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
    }
    public void clear() {
        taskStatusRepository.deleteById(testTaskStatus.getId());
    }
    @Test
    public void testIndexTaskStatus() throws Exception {
        mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk());
        clear();
    }

    @Test
    public void testCreateTaskStatus() throws Exception {
        clear();
        Map<String, String> data = Map.of(
                "name", "completed",
                "slug", "test_completed"
        );
        var request = post("/api/task_statuses").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var taskStatus = taskStatusRepository.findBySlug(data.get("slug")).get();

        Assertions.assertThat(taskStatus).isNotNull();
        Assertions.assertThat(taskStatus.getName()).isEqualTo(data.get("name"));
        Assertions.assertThat(taskStatus.getSlug()).isEqualTo(data.get("slug"));
        clear();
    }
    @Test
    public void testUpdateStatus() throws Exception {
        clear();
        taskStatusRepository.save(testTaskStatus);
        var data = new HashMap<>();
        data.put("name", "new Task Status");

        var request = put("/api/task_statuses/{id}", testTaskStatus.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testTaskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();
        assertThat(testTaskStatus.getName()).isEqualTo("new Task Status");
        clear();

    }
    @Test
    public void testShowTaskStatus() throws Exception {
        clear();
        taskStatusRepository.save(testTaskStatus);
        MockHttpServletRequestBuilder request = get("/api/task_statuses/{id}", testTaskStatus.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
        clear();

    }
    @Test
    public void testDeleteTaskStatus() throws Exception {
        clear();
        taskStatusRepository.save(testTaskStatus);
        var request = delete("/api/task_statuses/{id}", testTaskStatus.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isOk());
        assertThat(taskStatusRepository.existsById(testTaskStatus.getId())).isFalse();
    }
}
