package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class OfferMapper {


    private final IdGenerable idGenerator;

    public OfferResponseDto toDto(Offer offer) {
        return OfferResponseDto.builder()
                .url(offer.url())
                .jobTitle(offer.jobTitle())
                .companyName(offer.companyName())
                .salary(offer.salary())
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

    public Offer toOffer(OfferResponseFromServerDto dto) {
        return Offer.builder()
                .url(dto.offerUrl())
                .companyName(dto.company())
                .jobTitle(dto.title())
                .salary(dto.salary())
                .upperBoundSalary(dto.upperBoundSalary())
                .lowerBoundSalary(dto.lowerBoundSalary())
                .build();
    }

    public List<Offer> toOfferList(List<OfferResponseFromServerDto> dtos) {
        return dtos.stream()
                .map(this::toOffer)
                .toList();
    }

}
