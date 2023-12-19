package hexlet.code.util.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
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
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private Task testTask;
    private User testUser;
    private TaskStatus testTaskStatus;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private Faker faker;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        var user = userRepository.findByEmail("hexlet@example.com")
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        var taskStatus = taskStatusRepository.findBySlug("draft")
                .orElseThrow(() -> new RuntimeException("taskStatus not found"));

       var label = labelRepository.findByName("feature")
               .orElseThrow(() -> new RuntimeException("label not found"));

       testTask = Instancio.of(modelGenerator.getTaskModel()).create();

       testTask.setAssignee(user);
       testTask.setTaskStatus(taskStatus);
       testTask.setLabels(Set.of(label));

       taskRepository.save(testTask);
    }
    public void clear() {
        taskRepository.deleteById(testTask.getId());
    }
    @Test
    public void testIndexTask() throws Exception {
        mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk());
        clear();
    }

    @Test
    public void testCreateTask() throws Exception {
        clear();
        var data = Map.of(
                "name", faker.lorem().word(),
                "index", (Integer)faker.number().positive(),
                "assignee_id", 1,
                "description", faker.lorem().sentence(),
                "taskStatus", "draft"
        );
//        var data = Map.of(
//                "name", faker.name().name(),
//                "index", (Integer) faker.number().positive(),
//                "assignee_id", 1L,
//                "description", faker.lorem().sentence(),
//                "taskStatus", "draft",
//                "label", "feature"
//        );
        var request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Task testTask = taskRepository.findByName((String) data.get("name")).get();

        Assertions.assertThat(testTask).isNotNull();
        Assertions.assertThat(testTask.getName()).isEqualTo(data.get("name"));
        Assertions.assertThat(testTask.getIndex()).isEqualTo(data.get("index"));
        Assertions.assertThat(testTask.getDescription()).isEqualTo(data.get("description"));
        Assertions.assertThat(testTask.getTaskStatus()).isEqualTo(data.get("status"));
        clear();
    }
    @Test
    public void testUpdateTask() throws Exception {
        clear();
        taskRepository.save(testTask);
        var data = new HashMap<>();
        data.put("name", "new Task");

        var request = put("/api/tasks/{id}", testTask.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testTask = taskRepository.findById(testTask.getId()).get();
        assertThat(testTask.getName()).isEqualTo("new Task");
        clear();

    }
    @Test
    public void testShowTask() throws Exception {
        clear();
        taskRepository.save(testTask);
        MockHttpServletRequestBuilder request = get("/api/tasks/{id}", testTask.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTask.getName()),
                v -> v.node("index").isEqualTo(testTask.getIndex()),
                v -> v.node("description").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus())
        );
        clear();

    }
    @Test
    public void testDeleteTask() throws Exception {
        clear();
        taskRepository.save(testTask);
        var request = delete("/api/tasks/{id}", testTask.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isOk());
        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }

}
