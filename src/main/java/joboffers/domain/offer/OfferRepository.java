package joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {
    void save(Offer offer);
    Optional<Offer> findById(Long id);

    List<Offer> findAll();
}
