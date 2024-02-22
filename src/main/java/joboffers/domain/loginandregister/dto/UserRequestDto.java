package joboffers.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record UserRequestDto(
        String username,
        String password
) {
}
