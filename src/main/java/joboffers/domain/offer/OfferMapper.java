package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class OfferMapper {


    private final IdGenerable idGenerator;

    public  OfferResponseDto toDtoWithSalaryWithoutSalaryFork(Offer offer) {
        return OfferResponseDto.builder()
                .url(offer.url())
                .jobTitle(offer.jobTitle())
                .companyName(offer.companyName())
                .salary(offer.salary())
                .build();
    }

    public  OfferResponseDto toDtoWithSalaryFork(Offer offer) {
        return joboffers.domain.offer.dto.OfferResponseDto.builder()
                .url(offer.url())
                .jobTitle(offer.jobTitle())
                .companyName(offer.companyName())
                .lowerBoundSalary(offer.lowerBoundSalary())
                .upperBoundSalary(offer.upperBoundSalary())
                .build();
    }

    public Offer toOffer(OfferRequestDto offerResponseDto) {
        return Offer.builder()
                .id(idGenerator.generateId())
                .url(offerResponseDto.url())
                .jobTitle(offerResponseDto.jobTitle())
                .companyName(offerResponseDto.companyName())
                .lowerBoundSalary(offerResponseDto.lowerBoundSalary())
                .upperBoundSalary(offerResponseDto.upperBoundSalary())
                .build();
    }
    
}
