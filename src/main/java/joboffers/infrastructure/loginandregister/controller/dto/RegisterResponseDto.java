package joboffers.infrastructure.loginandregister.controller.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public record RegisterResponseDto(
        String message,
        HttpStatus status
) {
}
