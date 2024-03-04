package joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
class OfferConfiguration {

    @Bean
    public OfferRepository offerRepository() {
        return new OfferRepository() {
            @Override
            public void save(final Offer offer) {

            }

            @Override
            public Optional<Offer> findById(final String id) {
                return Optional.empty();
            }

            @Override
            public List<Offer> findAll() {
                return null;
            }

            @Override
            public void saveAll(final List<Offer> offers) {

            }
        };
    }

    @Bean
    public OfferFacade offerFacade(OfferFetchable offerFetchable, OfferRepository offerRepository) {
        OfferMapper offerMapper = new OfferMapper(
                new IdGenerator()
        );
        OfferFilter offerFilter = new OfferFilter();

        return new OfferFacade(offerRepository, offerMapper, offerFetchable, offerFilter);
    }

}
