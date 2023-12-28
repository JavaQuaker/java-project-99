package hexlet.code.util.controller;

import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.mapper.UserMapper;
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
import java.util.Map;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    private User testUser;
    private Task testTask;
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
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        System.out.println("testUser.getId() = " + testUser.getId());
        System.out.println("testUser.getEmail() = " + testUser.getEmail());
        System.out.println("testUser.getFirstName() = " + testUser.getFirstName());
        System.out.println("testUser.getLastName() = " + testUser.getLastName());
        System.out.println("testUser.getPassword() = " + testUser.getPassword());
        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk());
    }
    @Test
    public void testCreateUser() throws Exception {

        Map<String, String> data = Map.of(
                "email", faker.internet().emailAddress(),
                "firstName", faker.name().firstName(),
                "lastName", faker.name().lastName(),
                "password", faker.internet().password(3, 100)
        );
        var request = post("/api/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        Assertions.assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateUser() throws Exception {
        var data = Map.of(
                "firstName", faker.name().firstName(),
                "lastName", faker.name().lastName(),
                "email", faker.internet().emailAddress(),
                "password", faker.internet().password(3, 12)
        );

        var request = put("/api/users/{id}", testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testUser = userRepository.findById(testUser.getId()).get();

        System.out.println("qqqqq" + " " + "firstName" + " " + testUser.getFirstName());
        System.out.println("wwww" + " " + "firstname" + " " + data.get("firstName"));
        assertThat(testUser.getFirstName()).isEqualTo(data.get("firstName"));
        assertThat(testUser.getLastName()).isEqualTo(data.get("lastName"));
        assertThat(testUser.getEmail()).isEqualTo(data.get("email"));
        assertThat(testUser.getPassword()).isEqualTo(data.get("password"));
    }

    @Test
    public void testShowUser() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/users/{id}", testUser.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("email").isEqualTo(testUser.getEmail())
        );

    }
    @Test
    public void testDeleteUser() throws Exception {
        var request = delete("/api/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
               .andExpect(status().isOk());
        assertThat(userRepository.existsById(testUser.getId())).isFalse();

    }
}



