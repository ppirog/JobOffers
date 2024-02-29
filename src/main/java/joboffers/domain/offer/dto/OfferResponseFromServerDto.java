package joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseFromServerDto(
        String offerUrl,
        String title,
        String company,
        String salary,
        Long lowerBoundSalary,
        Long upperBoundSalary
) {
}
