package joboffers.domain.offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    private final Map<String, Offer> offers = new HashMap<>();


    @Override
    public void save(Offer offer) {
        offers.put(offer.id(), offer);
    }

    @Override
    public Optional<Offer> findById(final Long id) {
        return Optional.ofNullable(offers.get(id));
    }

    @Override
    public List<Offer> findAll() {
        return offers.values().stream().toList();
    }
}