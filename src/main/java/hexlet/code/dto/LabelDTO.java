package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Setter
@Getter
public class LabelDTO {
    private Long id;
    private JsonNullable<String> name;
    private LocalDate createdAt;
}
