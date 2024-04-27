package joboffers.domain.offer;

import joboffers.infrastructure.offer.controller.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final OfferFetchable offerFetcher;
    private final OfferFilter offerFilter;


    @Cacheable("offers")
    public List<OfferResponseDto> findAllOffers() {
        final List<Offer> all = offerRepository.findAll();
        log.info("All offers found");
        return all.stream()
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }


    @Cacheable("byId")
    public OfferResponseDto findOfferById(String id) {
        final Offer byId = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundInDatabaseException("Offer with id: " + id + " not found"));

        log.info("Offer with id: " + id + " found");
        return offerMapper.toDto(byId);
    }

    @CacheEvict(value = {"offers", "byId"}, allEntries = true)
    public OfferResponseDto saveOffer(OfferRequestDto requestDto) {
        final Offer offer = offerMapper.toOffer(requestDto);
        final Offer save = offerRepository.save(offer);
        log.info("Offer saved to database");
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
        log.info("Offers fetched from server and saved to database");
        return offersToAddToDatabase.stream()
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"offers", "byId"}, allEntries = true)
    public OfferResponseDto deleteOfferById(final String id) {

        final Offer byId = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundInDatabaseException("Offer with id: " + id + " not found"));

        offerRepository.deleteById(byId.id());
        log.info("Offer with id: " + id + " deleted");
        return offerMapper.toDto(byId);
    }
}
