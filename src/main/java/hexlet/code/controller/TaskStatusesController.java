package hexlet.code.controller;


import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/task_statuses")

public class TaskStatusesController {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;
    @Operation(summary = "Get list of all status")
    @ApiResponse(responseCode = "200", description = "List of all status",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskStatusDTO.class)) })
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskStatus>> index() {
        var users = taskStatusRepository.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }
    @Operation(summary = "Get status by id")
    @ApiResponse(responseCode = "200", description = "status found")
    @ApiResponse(responseCode = "400", description = "Status not found")
    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable long id) {
        var user = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id" + " " + id + " " + "not found"));
        return taskStatusMapper.map(user);
    }
    @Operation(summary = "Create new status")
    @ApiResponse(responseCode = "201", description = "Status created")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusData) {
        var taskStatus = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }
    @Operation(summary = "Update status by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status update"),
        @ApiResponse(responseCode = "404", description = "Status with that id not found")
    })
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatusDTO update(@RequestBody TaskStatusUpdateDTO taskStatusData, @PathVariable long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id" + " " +  id + " " + "not found"));
        taskStatusMapper.update(taskStatusData, taskStatus);
        taskStatusRepository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;

    }

    @Operation(summary = "Delete status by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status deleted"),
        @ApiResponse(responseCode = "404", description = "Status with that id not found")
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id" + " " +  id + " " + "not found"));
        taskStatusRepository.deleteById(id);
    }
}
