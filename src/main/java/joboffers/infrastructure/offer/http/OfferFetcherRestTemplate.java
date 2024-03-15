package joboffers.infrastructure.offer.http;

import joboffers.domain.offer.OfferFetchable;
import joboffers.domain.offer.dto.OfferResponseFromServerDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public List<OfferResponseFromServerDto> fetchAllOffers() {
//        http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers
        String urlForService = getUrlForService("/offers");
        HttpHeaders headers = new HttpHeaders();

        final String url = UriComponentsBuilder.fromHttpUrl(urlForService)
                .toUriString();

        final HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        final ParameterizedTypeReference<List<OfferResponseFromServerDto>> responseType = new ParameterizedTypeReference<>() {
        };

        try {
            final ResponseEntity<List<OfferResponseFromServerDto>> exchange = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    responseType
            );
            return exchange.getBody();
        } catch (ResourceAccessException e) {
            log.error("Error While fetching data" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String getUrlForService(final String s) {
        return uri + ":" + port + s;
    }
}
