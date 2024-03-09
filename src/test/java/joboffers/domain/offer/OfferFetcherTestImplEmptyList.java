package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferResponseFromServerDto;

import java.util.List;

class OfferFetcherTestImplEmptyList implements OfferFetchable {
    @Override
    public List<OfferResponseFromServerDto> fetchAllOffers() {
        return List.of();
    }
}
