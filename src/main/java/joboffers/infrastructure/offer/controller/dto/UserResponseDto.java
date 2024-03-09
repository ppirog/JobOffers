package joboffers.infrastructure.offer.controller.dto;

import joboffers.domain.offer.dto.OfferResponseDto;
import lombok.Builder;

import java.util.List;
@Builder
public record UserResponseDto(
        List<OfferResponseDto> offers
) {
}
