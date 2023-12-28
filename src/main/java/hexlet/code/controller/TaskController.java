package hexlet.code.controller;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskSpecification taskSpecification;


    public List<TaskDTO> find(TaskParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        Specification<Task> spec = taskSpecification.build(params);
        Page<Task> tasks = taskRepository.findAll(spec, PageRequest.of(page - 1, 10));
        Page<TaskDTO> result = tasks.map(taskMapper::map);
        return result.toList();

    }
    @Operation(summary = "Get list of all tasks")
    @ApiResponse(responseCode = "200", description = "List of all tasks",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskCreateDTO.class)) })

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        if (params != null) {
            List<TaskDTO> tasks = find(params, 1);
            System.out.println("tasks" + " " + tasks.toString());
            return ResponseEntity.ok()
                        .header("X-Total-Count", String.valueOf(tasks.size()))
                        .body(tasks);

        } else {
            List<Task> tasks = taskRepository.findAll();
            List<TaskDTO> result = tasks.stream()
                        .map(taskMapper::map)
                        .toList();
            return ResponseEntity.ok()
                        .header("X-Total-Count", String.valueOf(tasks.size()))
                        .body(result);
        }
    }

    @Operation(summary = "Get task by id")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "400", description = "Task not found")
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id" + " " + id + " " + "not found"));

        return taskMapper.map(task);
    }


    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @ApiResponse(responseCode = "400", description = "Task did not create")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
        Task task = taskMapper.map(taskData);

        System.out.println("task getId = " + task.getId());
        System.out.println("task getName = " + task.getName());
        System.out.println("task getIndex = " + task.getIndex());
        System.out.println("task getTaskStatus = " + task.getTaskStatus());
        System.out.println("task getAssignee = " + task.getAssignee());
        System.out.println("task getDescription = " + task.getDescription());

        Optional<TaskStatus> bySlug = taskStatusRepository.findBySlug(taskData.getStatus());
        bySlug.ifPresent(task::setTaskStatus);
        taskRepository.save(task);
        Optional<Task> byId = taskRepository.findById(task.getId());
//        var assigneeId = taskData.getAssigneeId();
//
//        if (assigneeId != null) {
//            var assignee = userRepository.findById(assigneeId).orElse(null);
//            task.setAssignee(assignee);
//        }
//
//        var statusSlug = taskData.getStatus();
//        var taskStatus = taskStatusRepository.findBySlug(statusSlug).orElse(null);
//
//        task.setTaskStatus(taskStatus);

        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }
    @Operation(summary = "Update task by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task update"),
        @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskDTO update(@RequestBody TaskUpdateDTO taskData, @PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id" + " " +  id + " " + "not found"));
        taskMapper.update(taskData, task);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;

    }
    @Operation(summary = "Delete task by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task deleted"),
        @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id" + " " +  id + " " + "not found"));
        taskRepository.deleteById(id);
    }
}


