package joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseFromServerDto(
        String url,
        String jobTitle,
        String companyName,
        Long salary,
        Long lowerBoundSalary,

        Long upperBoundSalary
) {
}
