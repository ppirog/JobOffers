package joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferRequestDto(
        String jobTitle,
        String companyName,
        Long salary,
        Long lowerBoundSalary,
        Long upperBoundSalary,
        String url
) {
}
