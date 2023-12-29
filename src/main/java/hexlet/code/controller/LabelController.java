package hexlet.code.controller;

import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;
    @Operation(summary = "Get list of all labels")
    @ApiResponse(responseCode = "200", description = "List of all labels",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LabelDTO.class)) })
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        List<Label> labels = labelRepository.findAll();
        List<LabelDTO> result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(result);
    }
    @Operation(summary = "Get label by id")
    @ApiResponse(responseCode = "200", description = "Label found")
    @ApiResponse(responseCode = "400", description = "Label not found")
    @GetMapping(path = "/{id}")
    public LabelDTO show(@PathVariable long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id" + " " + id + " " + "not found"));
        return labelMapper.map(label);
    }
    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label created")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO labelData) {
        var label = labelMapper.map(labelData);
        labelRepository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }
    @Operation(summary = "Update label by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label update"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO update(@RequestBody LabelUpdateDTO labelData, @PathVariable long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id" + " " +  id + " " + "not found"));
        labelMapper.update(labelData, label);
        labelRepository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;

    }
    @Operation(summary = "Delete label by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Label deleted"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        labelRepository.deleteById(id);
    }
}
