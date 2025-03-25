package epam.com.task_rest.dto;

import java.util.List;

public record UpdateTraineeTrainersListDto(String traineeUsername,
                                              List<String> trainers) {
}
