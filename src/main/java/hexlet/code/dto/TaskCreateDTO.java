package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class TaskCreateDTO {

    @NotNull
    private Long assigneeId;

    @NotBlank
    @Size(min = 1)
    private String title;

    private Integer index;

    private String content;


    @NotNull
    private String status;

    private Set<Long> taskLabelIds = new HashSet<>();
}
