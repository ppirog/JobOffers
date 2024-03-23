package joboffers.infrastructure.token.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record TokenErrorResponseDto(
        String message,
        HttpStatus status
) {
}
