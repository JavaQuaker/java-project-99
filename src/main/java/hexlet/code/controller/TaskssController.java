package hexlet.code.controller;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskssController {
    @Autowired
    private TaskSpecification taskSpecification;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskDTO> find(TaskParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        var spec = taskSpecification.build(params);
        var tasks = taskRepository.findAll(spec, PageRequest.of(page - 1, 10));
        var result = tasks.map(taskMapper::map);
        System.out.println("getTitle" + params.getTitleCont());
        System.out.println("getAssifneeId" + params.getAssigneeId());
        System.out.println("status" + params.getStatus());
        System.out.println("labelId" + params.getLabelId());
        return result;
    }
}
