package joboffers.infrastructure.apivalidation;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record DuplicateKeyExceptionDto(
        String message,
        HttpStatus status
) {
}
