package hexlet.code.controller;

import hexlet.code.dto.*;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")

public class TaskStatusesController {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskStatus>> index() {
        var users = taskStatusRepository.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }
    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable long id) {
        var user = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id" + " " + id + " " + "not found"));
        return taskStatusMapper.map(user);
    }
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusData) {
        var taskStatus = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatusDTO update(@RequestBody TaskStatusUpdateDTO taskStatusData, @PathVariable long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id" + " " +  id + " " + "not found"));
        taskStatusMapper.update(taskStatusData, taskStatus);
        taskStatusRepository.save(taskStatus);
        var TaskStatusDTO = taskStatusMapper.map(taskStatus);
        return TaskStatusDTO;

    }
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id" + " " +  id + " " + "not found"));
        taskStatusRepository.deleteById(id);
    }
}
