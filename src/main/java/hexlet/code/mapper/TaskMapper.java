package hexlet.code.mapper;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    public abstract TaskDTO map(Task model);
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "labels", source = "taskLabelIds")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract Task map(TaskCreateDTO dto);
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "labels", source = "taskLabelIds")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    public Set<Label> toLabelsSet(List<Long> taskLabelIds) {
        return new HashSet<>(labelRepository.findByIdIn(taskLabelIds).orElse(new HashSet<>()));
    }


}
