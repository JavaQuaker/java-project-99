package hexlet.code.model;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Task implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer index;

    @NotBlank
    @Size(min = 1)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
//    @ManyToOne(cascade = CascadeType.MERGE)
    @ManyToOne
    private TaskStatus taskStatus;


    @ManyToOne(fetch = FetchType.EAGER)
//    @ManyToOne(cascade = CascadeType.MERGE)
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;

//    @ManyToMany(cascade = CascadeType.MERGE)
    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    private Set<Label> labels = new HashSet<>();
}
