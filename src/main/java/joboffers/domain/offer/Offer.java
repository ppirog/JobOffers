package joboffers.domain.offer;

import lombok.Builder;

@Builder
record Offer(
        String id,
        String jobTitle,
        String companyName,
        Long salary,
        Long lowerBoundSalary,

        Long upperBoundSalary,
        String url
) {
}
