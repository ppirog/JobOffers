package joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseDto(
        String url,
        String jobTitle,
        String companyName,
        String salary,
        Long lowerBoundSalary,

        Long upperBoundSalary
) {
}
