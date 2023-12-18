package hexlet.code.mapper;

import hexlet.code.dto.*;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import org.mapstruct.*;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {
    public abstract LabelDTO map(Label model);

    public abstract Label map(LabelCreateDTO dto);

    public abstract void update(LabelUpdateDTO dto, @MappingTarget Label model);


}
