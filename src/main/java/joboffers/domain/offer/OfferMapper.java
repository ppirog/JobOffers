package joboffers.domain.offer;

import joboffers.infrastructure.offer.controller.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class OfferMapper {


    private final IdGenerable idGenerator;

    public OfferResponseDto toDto(Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.id())
                .url(offer.url())
                .jobTitle(offer.jobTitle())
                .companyName(offer.companyName())
                .salary(offer.salary())
                .build();
    }


    public Offer toOffer(OfferRequestDto offerResponseDto) {
        return Offer.builder()
                .id(idGenerator.generateId())
                .url(offerResponseDto.url())
                .jobTitle(offerResponseDto.jobTitle())
                .companyName(offerResponseDto.companyName())
                .salary(offerResponseDto.salary())
                .build();
    }

    public Offer toOffer(OfferResponseFromServerDto dto) {
        return Offer.builder()
                .url(dto.offerUrl())
                .companyName(dto.company())
                .jobTitle(dto.title())
                .salary(dto.salary())
                .build();
    }

    public List<Offer> toOfferList(List<OfferResponseFromServerDto> dtos) {
        return dtos.stream()
                .map(this::toOffer)
                .toList();
    }

}
