package hexlet.code.util.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private Task task;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private Faker faker;


}
