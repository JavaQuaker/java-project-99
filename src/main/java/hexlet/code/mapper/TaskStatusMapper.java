package hexlet.code.mapper;

import hexlet.code.dto.*;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import org.mapstruct.*;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatusDTO map(TaskStatus model);

    public abstract TaskStatus map(TaskStatusCreateDTO dto);

    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);

}
