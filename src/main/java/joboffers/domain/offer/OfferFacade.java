package joboffers.domain.offer;

import joboffers.infrastructure.offer.controller.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final OfferFetchable offerFetcher;
    private final OfferFilter offerFilter;

    @Cacheable("offers")
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


    public OfferResponseDto saveOffer(OfferRequestDto requestDto) {

        final Offer offer = offerMapper.toOffer(requestDto);

        final Offer save = offerRepository.save(offer);

        return offerMapper.toDto(save);
    }

    public List<OfferResponseDto> fetchNewOffersAndSaveToDatabase() {
        final List<Offer> all = offerRepository.findAll();

        final List<OfferResponseFromServerDto> offerResponseFromServerDtos = offerFetcher.fetchAllOffers();

        final List<Offer> collected = offerMapper.toOfferList(offerResponseFromServerDtos);

        final List<Offer> offersToAddToDatabase = offerFilter.filterByUrl(all, collected);

        if(!offersToAddToDatabase.isEmpty()){
            offerRepository.saveAll(offersToAddToDatabase);
        }

        return offersToAddToDatabase.stream()
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }
}
