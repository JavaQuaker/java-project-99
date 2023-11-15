package hexlet.code.app.dto;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
}
