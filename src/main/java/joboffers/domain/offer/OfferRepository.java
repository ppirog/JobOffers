package joboffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
interface OfferRepository extends MongoRepository<Offer, String> {


    Optional<Offer> findById(String id);

    List<Offer> findAll();

}
