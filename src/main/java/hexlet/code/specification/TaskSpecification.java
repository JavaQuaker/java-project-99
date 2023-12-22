package hexlet.code.specification;

import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.model.Task;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withAssigneeId(params.getAssigneeId())
                .and(withTitleCont(params.getTitleCont()))
                .and(withTitleCont(params.getTitleCont()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getTitleCont()));


    }
    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"),
                assigneeId);
    }
    private Specification<Task> withTitleCont(String titleCont) {
        return (root, query, cb) -> titleCont == null ? cb.conjunction() : cb.equal(root.get("titleCont").get("id"),
                titleCont);
    }
    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status").get("id"),
                status);
    }
    private Specification<Task> withLabelId(String labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction() : cb.equal(root.get("labelId").get("id"),
                labelId);
    }
}


