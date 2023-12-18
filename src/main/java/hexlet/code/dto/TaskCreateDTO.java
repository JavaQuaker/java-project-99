package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDTO {

    @NotNull
    private Long assigneeId;

    @NotNull
    @Size(min = 1)
    private String name;

//    @NotBlank
//    private Integer index;

    @NotNull
    private String description;

    @NotNull
    private String taskStatus;
}
