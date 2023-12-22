package hexlet.code.util.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;
    private User testUser;
    private Task testTask;
    private Label testLabels;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private LabelMapper labelMapper;
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
        testLabels = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabels);
    }
    public void clear() {
        labelRepository.deleteById(testLabels.getId());
    }

    @Test
        public void testIndex() throws Exception {
        mockMvc.perform(get("/api/labels").with(jwt()))
                    .andExpect(status().isOk());
    }
    @Test
    public void testCreateLabel() throws Exception {

        Map<String, String> data = Map.of(

                "name", faker.name().name()
         );
        var request = post("/api/labels").with(token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                    .andExpect(status().isCreated());

        var labels = labelRepository.findByName(testLabels.getName()).get();

        Assertions.assertThat(labels).isNotNull();
        Assertions.assertThat(labels.getName()).isEqualTo(testLabels.getName());
    }

    @Test
    public void testUpdateLabel() throws Exception {
        labelRepository.save(testLabels);
        var data = new HashMap<>();
        data.put("name", "Nick");

        var request = put("/api/labels/{id}", testLabels.getId()).with(token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                    .andExpect(status().isOk());
        testLabels = labelRepository.findById(testLabels.getId()).get();
        assertThat(testLabels.getName()).isEqualTo("Nick");


    }
    @Test
    public void testShowLabel() throws Exception {
        labelRepository.save(testLabels);
        MockHttpServletRequestBuilder request = get("/api/labels/{id}", testLabels.getId()).with(token);
        var result = mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                    v -> v.node("name").isEqualTo(testLabels.getName())

        );

    }
    @Test
    public void testDeleteLabel() throws Exception {
        labelRepository.save(testLabels);
        var request = delete("/api/labels/{id}", testLabels.getId()).with(token);
        mockMvc.perform(request)
                    .andExpect(status().isOk());
        assertThat(labelRepository.existsById(testLabels.getId())).isFalse();

    }
}


