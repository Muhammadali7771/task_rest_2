package epam.com.task_rest.dto.trainee;


public record TraineeShortDto(String username,
                              String firstName,
                              String lastName,
                              boolean isActive
) {
}
