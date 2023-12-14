package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TaskStatusCreateDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String slug;

}
