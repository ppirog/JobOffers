package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public List<OfferResponseDto> findAllOffers() {
        final List<Offer> all = offerRepository.findAll();
        return all.stream()
                .map(
                        offer -> offer.lowerBoundSalary() != null && offer.upperBoundSalary() != null ?
                                offerMapper.toDtoWithSalaryFork(offer) :
                                offerMapper.toDtoWithSalaryWithoutSalaryFork(offer)
                )
                .collect(Collectors.toList());
    }

//    public OfferResponseDto findOfferById(Long id) {
//        final Offer byId = offerRepository.findById(id);
//    }
//
    public void saveOffer(OfferRequestDto requestDto) {

        final Offer offer = offerMapper.toOffer(requestDto);

        offerRepository.save(offer);
    }
//
//    public List<OfferResponseDto> fetchAllOffersAndSavellIfNotExist() {
//
//    }
}
