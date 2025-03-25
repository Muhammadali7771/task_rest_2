package epam.com.task_rest.controller;

import epam.com.task_rest.dto.training.TrainingCreateDto;
import epam.com.task_rest.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gym.com/api/training")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(summary = "Creates new training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully creates new Training"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<Void> add(@RequestBody @Valid TrainingCreateDto trainingCreateDto) {
        trainingService.create(trainingCreateDto);
        return ResponseEntity.ok().build();
    }

}
