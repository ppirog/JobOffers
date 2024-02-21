package joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseDto(
        String url,
        String jobTitle,
        String companyName,
        Long salary,
        Long lowerBoundSalary,

        Long upperBoundSalary
) {
}
