package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<Long> assigneeId;

    @NotBlank
    private JsonNullable<String> name;

    @NotNull
    private JsonNullable<Integer> index;

    @NotBlank
    private JsonNullable<String> description;

    @NotBlank
    private JsonNullable<String> taskStatus;




}
