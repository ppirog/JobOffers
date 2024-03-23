package joboffers.infrastructure.offer.scheduler;

import joboffers.domain.offer.OfferFacade;
import joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
class OfferFetcherScheduler {

    private final OfferFacade offerFacade;

    @Scheduled(cron = "${job-offers.offer-fetcher.scheduler.cron}")
    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExist() {
        log.info("Fetching offers");
        final List<OfferResponseDto> offerResponseDtos = offerFacade.fetchNewOffersAndSaveToDatabase();
        log.info("Offers fetched: " + offerResponseDtos);
        return offerResponseDtos;
    }
}
