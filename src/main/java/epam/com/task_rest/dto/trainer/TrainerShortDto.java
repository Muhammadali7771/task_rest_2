package epam.com.task_rest.dto.trainer;


public record TrainerShortDto(String username,
                              String firstName,
                              String lastName,
                              boolean isActive,
                              Integer specializationId) {
}
