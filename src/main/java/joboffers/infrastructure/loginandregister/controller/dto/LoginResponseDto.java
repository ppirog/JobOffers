package joboffers.infrastructure.loginandregister.controller.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record LoginResponseDto(
        String message,
        HttpStatus status
) {
}
