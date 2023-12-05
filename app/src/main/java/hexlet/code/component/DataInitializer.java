package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final CustomUserDetailsService userService;
    @Autowired
    private final UserRepository userRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setFirstName("hexlet");
        userData.setEmail("hexlet@example@com");
        userData.setPasswordDigest("123");
        var user = userMapper.map(userData);
        userRepository.save(user);
    }
}
