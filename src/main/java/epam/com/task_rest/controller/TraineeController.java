package epam.com.task_rest.controller;

import epam.com.task_rest.dto.ChangeLoginDto;
import epam.com.task_rest.dto.LoginRequestDto;
import epam.com.task_rest.dto.RegistrationResponseDto;
import epam.com.task_rest.dto.UpdateTraineeTrainersListDto;
import epam.com.task_rest.dto.trainee.TraineeCreateDto;
import epam.com.task_rest.dto.trainee.TraineeDto;
import epam.com.task_rest.dto.trainee.TraineeStatusUpdateDto;
import epam.com.task_rest.dto.trainee.TraineeUpdateDto;
import epam.com.task_rest.dto.trainer.TrainerShortDto;
import epam.com.task_rest.dto.training.TrainingDto;
import epam.com.task_rest.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("gym.com/api/trainee")
public class TraineeController {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @GetMapping
    @Operation(summary = "Get trainee with the supplied username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainee with the supplied username"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeDto> getTraineeByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(traineeService.getTraineeByUsername(username));
    }

    @PostMapping
    @Operation(summary = "Register a new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Trainee successfully has been registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")})
    public ResponseEntity<RegistrationResponseDto> register(@RequestBody @Valid TraineeCreateDto dto) {
        return new ResponseEntity<>(traineeService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update trainee profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the trainee information"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "The Resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<TraineeDto> update(@RequestParam("username") String username, @RequestBody TraineeUpdateDto dto) {
        TraineeDto updatedTrainee = traineeService.update(username, dto);
        return ResponseEntity.ok(updatedTrainee);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication failed"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto dto) {
        traineeService.login(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Deleting the specific trainee with the supplied username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deletes the specific trainee"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> delete(@RequestParam("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-login")
    @Operation(summary = "Change the password of the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginDto dto) {
        traineeService.changePassword(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainer-list")
    @Operation(summary = "Update trainee's trainer list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updates the trainee's trainer list"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainerShortDto>> updateTraineeTrainerList(@RequestBody UpdateTraineeTrainersListDto dto) {
        List<TrainerShortDto> trainers = traineeService.updateTraineeTrainerList(dto);
        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }

    @GetMapping("training-list")
    @Operation(summary = "Get trainee trainings list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieves the trainings of the trainee supplied with the username by criteria"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainingDto>> getTrainingList(@RequestParam(name = "username") String traineeUsername,
                                                             @RequestParam(name = "period-from", required = false) Date periodFrom,
                                                             @RequestParam(name = "period-to", required = false) Date periodTo,
                                                             @RequestParam(name = "trainer-name", required = false) String trainerName,
                                                             @RequestParam(name = "training-type", required = false) Integer trainingTypeId) {
        List<TrainingDto> trainings = traineeService.getTraineeTrainingsList(traineeUsername, periodFrom, periodTo, trainerName, trainingTypeId);
        return ResponseEntity.ok(trainings);
    }

    @PatchMapping("/change-status/{username}")
    @Operation(summary = "Activate/De-Activate trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activates or deactivates the trainee"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<Void> activateOrDeactivateTrainee(@PathVariable String username) {
        traineeService.activateOrDeactivateTrainee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unassigned-trainers")
    @Operation(summary = "Get the trainers with is not assigned on trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieves trainers which is not assigned on trainee"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    public ResponseEntity<List<TrainerShortDto>> getUnassignedTrainers(@RequestParam("username") String traineeUsername) {
        List<TrainerShortDto> trainers = traineeService
                .getTrainersListNotAssignedOnTrainee(traineeUsername);
        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }

}
