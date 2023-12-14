package hexlet.code.specification;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    private Specification<TaskStatus> withTaskStatusId(Long taskStatusId) {
        return (root, query, cb) -> taskStatusId == null ? cb.conjunction() : cb.equal(root.get("taskStatus").get("id"),
                taskStatusId);
    }
}
