package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
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
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.util.Collections;
import java.util.Map;
import java.util.Set;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

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
    private TaskMapper taskMapper;
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

        var taskStatus = taskStatusRepository.findBySlug("published")
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

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setIndex((Integer) faker.number().positive());
        dto.setAssigneeId(1L);
        dto.setTitle(faker.lorem().word());
        dto.setContent(faker.lorem().sentence());
        dto.setStatus("draft");
        dto.setTaskLabelIds(Collections.singleton(1L));

        var request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName((String) dto.getTitle()).orElse(null);
        System.out.println("task" + " " + task.getName());
        System.out.println("data" + " " + dto.getTitle());

        assertThat(task).isNotNull();
        assertThat(task.getIndex()).isEqualTo(dto.getIndex());
        assertThat(task.getName()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getContent());
        assertThat(task.getTaskStatus().getName()).isEqualTo("draft");
        assertThat(task.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
        assertThat(task.getLabels().iterator().next().getId()).isEqualTo(1L);

    }
    @Test
    public void testWithoutLabel() throws Exception {

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setIndex((Integer) faker.number().positive());
        dto.setAssigneeId(1L);
        dto.setTitle(faker.lorem().word());
        dto.setContent(faker.lorem().sentence());
        dto.setStatus("draft");
        var request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        var task = taskRepository.findByName((String) dto.getTitle()).orElseThrow(null);
        System.out.println(task.getLabels());
    }
    @Test
    public void testUpdateTask() throws Exception {
        var user = userRepository.findByEmail("hexlet@example.com").orElseThrow(null);
        var dto = new TaskUpdateDTO();
        dto.setTitle(JsonNullable.of("new name"));
        dto.setStatus(JsonNullable.of("published"));
        dto.setContent(JsonNullable.of("new content"));
        dto.setIndex(JsonNullable.of(123));
        dto.setAssigneeId(JsonNullable.of(user.getId()));

        var request = put("/api/tasks/{id}", testTask.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testTask = taskRepository.findById(testTask.getId()).get();
        System.out.println("testName" + " " + dto.getTitle());
        assertThat(testTask.getName()).isEqualTo(dto.getTitle().get());
        assertThat(testTask.getTaskStatus().getSlug()).isEqualTo(dto.getStatus().get());
        assertThat(testTask.getDescription()).isEqualTo(dto.getContent().get());
        assertThat(testTask.getIndex()).isEqualTo(dto.getIndex().get());
        assertThat(testTask.getAssignee().getId()).isEqualTo(dto.getAssigneeId().get());

    }
    @Test
    public void testShowTask() throws Exception {

        taskRepository.save(testTask);

        System.out.println("title" + " " + testTask.getName());
        System.out.println("index" + " " + testTask.getIndex());
        System.out.println("content" + " " + testTask.getDescription());
        System.out.println("assignee" + " " + testTask.getAssignee());
        System.out.println("status" + " " + testTask.getTaskStatus());
        MockHttpServletRequestBuilder request = get("/api/tasks/{id}", testTask.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("index").isEqualTo(testTask.getIndex()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getName())
        );


    }
    @Test
    public void testDeleteTask() throws Exception {

        taskRepository.save(testTask);
        var request = delete("/api/tasks/{id}", testTask.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }

}
