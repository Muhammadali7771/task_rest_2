package epam.com.task_rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotBlank(message = "username can not be null and blank")
        String username,
        @NotBlank(message = "password can not be null and blank")
        String password) {
}
