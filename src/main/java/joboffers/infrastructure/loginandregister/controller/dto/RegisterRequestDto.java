package joboffers.infrastructure.loginandregister.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
