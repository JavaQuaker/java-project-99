package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter

public class TaskDTO {
    private Long id;
    private Integer index;
//    private String name;
    private String title;
    //private String description;
    private String content;
   // private String taskStatus;
    private String status;
    private Long assigneeId;
    private LocalDate createdAt;
}
