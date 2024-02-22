package joboffers.domain.offer;

import joboffers.domain.offer.dto.OfferResponseFromServerDto;

import java.util.List;

interface OfferFetchable {

    List<OfferResponseFromServerDto> fetchAllOffers();
}
