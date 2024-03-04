package joboffers.domain.offer;

import lombok.Builder;

@Builder
record Offer(
        String id,
        String jobTitle,
        String companyName,
        String salary,
        Long lowerBoundSalary,
        Long upperBoundSalary,
        String url
) {
}
