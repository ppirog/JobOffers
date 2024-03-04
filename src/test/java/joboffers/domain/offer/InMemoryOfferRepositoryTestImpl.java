package joboffers.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    private final Map<String, Offer> offers = new HashMap<>();


    @Override
    public <S extends Offer> S save(final S entity) {
        offers.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Offer> findById(final String id) {
        return Optional.ofNullable(offers.get(id));
    }

    @Override
    public boolean existsById(final String s) {
        return false;
    }


    @Override
    public <S extends Offer> List<S> saveAll(final Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();

        entities.forEach(entity -> {
            offers.put(entity.id(), entity);
            savedEntities.add(entity);
        });

        return savedEntities;

    }

    @Override
    public List<Offer> findAll() {
        return offers.values().stream().toList();
    }

    @Override
    public List<Offer> findAllById(final Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final String s) {

    }

    @Override
    public void delete(final Offer entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(final Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

//    @Override
//    public void saveAll(final List<Offer> offers) {
//        offers.forEach(offer -> this.offers.put(offer.id(), offer));
//    }

    @Override
    public <S extends Offer> S insert(final S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(final Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(final Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(final Example<S> example, final Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Offer> findAll(final Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(final Pageable pageable) {
        return null;
    }
}
