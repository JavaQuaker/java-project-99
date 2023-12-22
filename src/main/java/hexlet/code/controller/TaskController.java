package hexlet.code.controller;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;


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


    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(result);
    }
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id" + " " + id + " " + "not found"));

        return taskMapper.map(task);
    }



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
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

//    @PostMapping("")
//    @ResponseStatus(HttpStatus.CREATED)
//    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
//        var task = toEntity(taskData);
//        taskRepository.save(task);
//        var taskDTO = toDTO(task);
//        return taskDTO;
//    }

//    private TaskDTO toDTO(Task task) {
//        var dto = new TaskDTO();
//        dto.setIndex(task.getIndex());
//        dto.setDescription(task.getDescription());
//        dto.setTaskStatus(task.getTaskStatus());
//        dto.setAssigneeId(task.getAssignee().getId());
//        dto.setCreatedAt(task.getCreatedAt());
//        return dto;
//    }
//
//    private Task toEntity(TaskCreateDTO taskDto) {
//        var task = new Task();
//        task.setAssignee(taskDto.getAssigneeId());
//        task.setName(taskDto.getName());
//        task.setDescription(taskDto.getDescription());
//        task.setTaskStatus(taskDto.getTaskStatus());
//        return task;
//    }





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
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id" + " " +  id + " " + "not found"));
        taskRepository.deleteById(id);
    }
}


