package epam.com.task_rest.dto.trainer;


public record TrainerUpdateDto(String firstName,
                               String lastName,
                               boolean isActive,
                               Integer specializationId) {
}
