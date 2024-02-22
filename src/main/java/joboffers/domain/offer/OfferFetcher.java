package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferResponseFromServerDto;

import java.util.List;

class OfferFetcher implements OfferFetchable{
    @Override
    public List<OfferResponseFromServerDto> fetchAllOffers() {
        return List.of();
    }
}
