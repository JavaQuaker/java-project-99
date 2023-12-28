package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TaskCreateDTO {

//    @NotNull
    private Long assigneeId;

    @NotBlank
    @Size(min = 1)
    private String title;

    private Integer index;

    private String content;


    @NotNull
    private String status;

    private List<Long> taskLabelIds = new ArrayList<>();
}
