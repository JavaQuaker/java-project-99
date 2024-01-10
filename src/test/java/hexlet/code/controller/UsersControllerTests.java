package hexlet.code.controller;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JWTUtils;
import hexlet.code.util.ModelGenerator;
import hexlet.code.mapper.UserMapper;
import hexlet.code.util.UserUtils;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
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
    private UserUtils userUtils;
    @Autowired
    private JWTUtils jwtUtils;
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

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(faker.internet().emailAddress());
        dto.setFirstName(faker.name().firstName());
        dto.setLastName(faker.name().lastName());
        dto.setPassword(faker.internet().password(3, 100));

        var request = post("/api/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        Assertions.assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateUser() throws Exception {

        var dto = new UserUpdateDTO();
        dto.setFirstName(JsonNullable.of(faker.name().firstName()));
        dto.setLastName(JsonNullable.of(faker.name().lastName()));
        dto.setEmail(JsonNullable.of(faker.internet().emailAddress()));
        dto.setPassword(JsonNullable.of(faker.internet().password(3, 12)));

        var request = put("/api/users/{id}", testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testUser = userRepository.findById(testUser.getId()).get();

        System.out.println("qqqqq" + " " + "firstName" + " " + testUser.getFirstName());
        System.out.println("wwww" + " " + "firstname" + " " + dto.getFirstName());
        assertThat(testUser.getFirstName()).isEqualTo(dto.getFirstName().get());
        assertThat(testUser.getLastName()).isEqualTo(dto.getLastName().get());
        assertThat(testUser.getEmail()).isEqualTo(dto.getEmail().get());
        assertThat(testUser.getPassword()).isEqualTo(dto.getPassword().get());
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
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        var request = delete("/api/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(userRepository.existsById(testUser.getId())).isFalse();

    }
    @Test
    public void testDeleteAnotherUser() throws Exception {
        var request = delete("/api/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(userRepository.existsById(testUser.getId())).isTrue();
    }
}
