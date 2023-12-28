package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Setter
@Getter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<Long> assigneeId;

    @NotBlank
    private JsonNullable<String> title;

    @NotNull
    private JsonNullable<Integer> index;

    @NotBlank
    private JsonNullable<String> content;

    @NotBlank
    private JsonNullable<String> status;

    @NotNull
    private JsonNullable<List<Long>> taskLabelIds;
}
