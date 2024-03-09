package joboffers.domain.offer;

import joboffers.infrastructure.offer.controller.dto.OfferRequestDto;
import joboffers.domain.offer.dto.OfferResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfferFacadeTest {


    @Test
    public void should_properly_save_offers() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGenerator()
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url")
                        .jobTitle("jobTitle")

                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url2")
                        .jobTitle("jobTitle2")
                        .companyName("companyName2")
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
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url1")
                        .jobTitle("jobTitle1")
                        .companyName("companyName1")

                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url2")
                        .jobTitle("jobTitle2")
                        .companyName("companyName2")

                        .build()

        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("url3")
                        .jobTitle("jobTitle3")
                        .companyName("companyName3")
                        .build()
        );

        final List<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        final Set<String> collect = allOffers.stream()
                .map(OfferResponseDto::url)
                .collect(Collectors.toSet());

        assertEquals(3, allOffers.size());
        assertTrue(collect.contains("url1"));
        assertTrue(collect.contains("url2"));
        assertTrue(collect.contains("url3"));
    }

    @Test
    public void should_properly_return_offer_by_id_test_1() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGeneratorTestImpl()
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("urlTested")
                        .jobTitle("jobTitle")
                        .companyName("companyName")

                        .build()

        );

        final OfferResponseDto offerById = offerFacade.findOfferById("test-id");

        assertEquals("urlTested", offerById.url());
    }

    @Test
    public void should_throw_not_found_in_database_exception() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGeneratorTestImpl()
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        assertThrows(NotFoundInDatabaseException.class, () -> offerFacade.findOfferById("test-id"));
    }


    @Test
    public void should_properly_fetch_all_offers_and_save_if_not_exist() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGeneratorTestImpl()
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("urlTested")
                        .jobTitle("jobTitle")
                        .companyName("companyName")

                        .build()

        );

        final List<OfferResponseDto> allOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();

        assertEquals(2, allOffers.size());
    }

    @Test
    public void should_properly_fetch_all_offers_and_save_if_not_exist_test_2() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGeneratorTestImpl()
                ),
                new OfferFetcherTestImplEmptyList(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("https://www.offer2.com")
                        .jobTitle("jobTitle")
                        .companyName("companyName")

                        .build()

        );

        final List<OfferResponseDto> allOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();

        assertEquals(0, allOffers.size());
    }

    @Test
    public void should_properly_fetch_all_offers_filter_and_save_if_not_exist() {

        OfferFacade offerFacade = new OfferFacade(
                new InMemoryOfferRepositoryTestImpl(),
                new OfferMapper(
                        new IdGeneratorTestImpl()
                ),
                new OfferFetcherTestImpl(),
                new OfferFilter()
        );

        offerFacade.saveOffer(
                OfferRequestDto.builder()
                        .url("https://www.offer2.com")
                        .jobTitle("jobTitle")
                        .companyName("companyName")

                        .build()

        );

        final List<OfferResponseDto> allOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();

        assertEquals(1, allOffers.size());
    }



}