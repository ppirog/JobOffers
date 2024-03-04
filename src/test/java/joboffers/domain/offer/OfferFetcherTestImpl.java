package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferResponseFromServerDto;

import java.util.List;

class OfferFetcherTestImpl implements OfferFetchable{
    @Override
    public List<OfferResponseFromServerDto> fetchAllOffers() {
        final OfferResponseFromServerDto of1 = OfferResponseFromServerDto.builder()
                .offerUrl("https://www.offer.com")
                .title("Software Developer")
                .company("Offer")
                .salary("1000L")
                .lowerBoundSalary(1000L)
                .upperBoundSalary(1000L)
                .build();

        final OfferResponseFromServerDto of2 = OfferResponseFromServerDto.builder()
                .offerUrl("https://www.offer2.com")
                .title("Software Developer")
                .company("Offer2")
                .salary("2000L")
                .lowerBoundSalary(2000L)
                .upperBoundSalary(2000L)
                .build();

        return List.of(of1, of2);
    }
}
