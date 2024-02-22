package joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class OfferFilter {

    public List<Offer> filterByUrl(List<Offer> all, List<Offer> collected) {
        final List<String> idList = all
                .stream()
                .map(Offer::url).toList();

        return collected
                .stream()
                .filter(offer -> !idList.contains(offer.url()))
                .toList();
    }
}
