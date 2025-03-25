package epam.com.task_rest.dto.trainee;


import java.util.Date;


public record TraineeUpdateDto(String firstName,
                               String lastName,
                               boolean isActive,
                               Date dateOfBirth,
                               String address) {
}

