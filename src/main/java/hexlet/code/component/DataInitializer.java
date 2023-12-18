package hexlet.code.component;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final CustomUserDetailsService userService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TaskStatusRepository taskStatusRepository;
    @Autowired
    private final TaskStatusMapper taskStatusMapper;
    @Autowired
    private final LabelRepository labelRepository;
    @Autowired
    private final LabelMapper labelMapper;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        defaultUser();
        defaultTaskStatus();
        defaultLabels();

    }
    public void defaultUser() {
        UserCreateDTO userData = new UserCreateDTO();
        userData.setFirstName("hexlet");
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        User user = userMapper.map(userData);
        userRepository.save(user);
    }
    public void defaultTaskStatus() {
        TaskStatusCreateDTO taskStatusData = new TaskStatusCreateDTO();
        taskStatusData.setName("draft");
        taskStatusData.setSlug("draft");
        TaskStatus taskStatus = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus);

        taskStatusData.setName("to_review");
        taskStatusData.setSlug("to_review");
        TaskStatus taskStatus1 = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus1);

        taskStatusData.setName("to_be_fixed");
        taskStatusData.setSlug("to_be_fixed");
        TaskStatus taskStatus2 = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus2);

        taskStatusData.setName("to_publish");
        taskStatusData.setSlug("to_publish");
        TaskStatus taskStatus3 = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus3);

        taskStatusData.setName("published");
        taskStatusData.setSlug("published");
        TaskStatus taskStatus4 = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus4);

    }
    public void defaultLabels() {
//        LabelCreateDTO labelCreateDTO = new LabelCreateDTO();
//        labelCreateDTO.setName("feature");
//        Label labelFeature = labelMapper.map(labelCreateDTO);
//        labelRepository.save(labelFeature);
//
//        labelCreateDTO.setName("bug");
//        Label labelBug = labelMapper.map(labelCreateDTO);
//        labelRepository.save(labelBug);
        List<String> defLabels = List.of("feature", "bug");
        defLabels.forEach(name -> {
            LabelCreateDTO labelCreateDTO = new LabelCreateDTO();
            labelCreateDTO.setName(name);
            Label label = labelMapper.map(labelCreateDTO);
            labelRepository.save(label);
        });
    }
}
