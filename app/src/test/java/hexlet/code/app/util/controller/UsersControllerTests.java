package hexlet.code.app.util.controller;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.ModelGenerator;
import hexlet.code.app.mapper.UserMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
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
    private User testUser;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private ObjectMapper om;
    private JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
//        userRepository.save(testUser);
    }
    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/users").with(jwt()))
                .andExpect(status().isOk());
    }
    @Test
    public void testCreateUser() throws Exception {
        UserDTO dto = mapper.map(testUser);
        var request = post("/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }
    @Test
    public void testUpdateUser() throws Exception {
        userRepository.save(testUser);
        var data = new HashMap<>();
        data.put("firstName", "Nick");

        var request = put("/users/{id}", testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        testUser = userRepository.findById(testUser.getId()).get();
        assertThat(testUser.getFirstName()).isEqualTo("Nick");

    }
    @Test
    public void testShowUser() throws Exception {
        userRepository.save(testUser);
        MockHttpServletRequestBuilder request = get("/users/{id}", testUser.getId()).with(token);
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
        userRepository.save(testUser);
        var request = delete("/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
               .andExpect(status().isOk());
        assertThat(userRepository.existsById(testUser.getId())).isFalse();

    }
}



