package joboffers.infrastructure.offer.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OfferRequestDto(
        @NotNull(message = "{jobTitle.not.null}")
        @NotEmpty(message = "{jobTitle.not.empty}")
        String jobTitle,
        @NotNull(message = "{companyName.not.null}")
        @NotEmpty(message = "{companyName.not.empty}")
        String companyName,
        @NotNull(message = "{salary.not.null}")
        @NotEmpty(message = "{salary.not.empty}")
        String salary,
        @NotNull(message = "{url.not.null}")
        @NotEmpty(message = "{url.not.empty}")
        String url
) {
}
