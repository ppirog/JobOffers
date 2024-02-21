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
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }

    public OfferResponseDto findOfferById(String id) {
        final Offer byId = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundInDatabaseException("Offer with id: " + id + " not found"));

        return offerMapper.toDto(byId);
    }

    //
    public void saveOffer(OfferRequestDto requestDto) {

        final Offer offer = offerMapper.toOffer(requestDto);

        offerRepository.save(offer);
    }

//    public List<OfferResponseDto> fetchAllOffersAndSavellIfNotExist() {
//        final List<Offer> all = offerRepository.findAll();
//        return all.stream()
//                .map(offerMapper::toDto)
//                .collect(Collectors.toList());
//    }
}
