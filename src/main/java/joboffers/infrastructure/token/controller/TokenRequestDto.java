package joboffers.infrastructure.token.controller;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
