package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final OfferFetchable offerFetcher;
    private final OfferFilter offerFilter;

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


    public void saveOffer(OfferRequestDto requestDto) {

        final Offer offer = offerMapper.toOffer(requestDto);

        offerRepository.save(offer);
    }

    public List<OfferResponseDto> fetchAllOffersAndSavellIfNotExist() {
        final List<Offer> all = offerRepository.findAll();

        final List<OfferResponseFromServerDto> offerResponseFromServerDtos = offerFetcher.fetchAllOffers();

        final List<Offer> collected = offerMapper.toOfferList(offerResponseFromServerDtos);

        final List<Offer> offersToAddToDatabase = offerFilter.filterByUrl(all, collected);

        offerRepository.saveAll(offersToAddToDatabase);

        return offersToAddToDatabase.stream()
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }
}
