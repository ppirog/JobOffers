package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferFacadeTest {


    @Test
    public void should_properly_save_offers() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGenerator()
                )
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url")
                        .jobTitle("jobTitle")
                        .companyName("companyName")
                        .lowerBoundSalary(1000L)
                        .upperBoundSalary(2000L)
                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url2")
                        .jobTitle("jobTitle2")
                        .companyName("companyName2")
                        .lowerBoundSalary(12000L)
                        .upperBoundSalary(22000L)
                        .build()

        );

        final List<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        assertEquals(2, allOffers.size());
    }

    @Test
    public void should_properly_return_all_offers() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGenerator()
                )
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url")
                        .jobTitle("jobTitle")
                        .companyName("companyName")
                        .lowerBoundSalary(1000L)
                        .upperBoundSalary(2000L)
                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url2")
                        .jobTitle("jobTitle2")
                        .companyName("companyName2")
                        .lowerBoundSalary(12000L)
                        .upperBoundSalary(22000L)
                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url3")
                        .jobTitle("jobTitle3")
                        .companyName("companyName3")
                        .salary(12000L)
                        .build()
        );

        final List<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        assertEquals(3, allOffers.size());
    }




}