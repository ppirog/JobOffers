package joboffers.infrastructure.offer.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record NotFoundInDatabaseResponseDto(
        HttpStatus status,
        String message
) {
}
