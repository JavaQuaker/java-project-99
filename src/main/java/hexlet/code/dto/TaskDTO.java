package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter

public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String description;
    private String taskStatus;
    private Long assigneeId;
    private LocalDate createdAt;
}
