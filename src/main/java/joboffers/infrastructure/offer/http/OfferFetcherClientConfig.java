package joboffers.infrastructure.offer.http;

import joboffers.domain.offer.OfferFetchable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
@Configuration
public class OfferFetcherClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(7000))
                .setReadTimeout(Duration.ofMillis(7000))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferFetcherClient(RestTemplate restTemplate,
                                                   @Value("${job-offers.offer-fetcher.http.client.config.uri}") String uri,
                                                   @Value("${job-offers.offer-fetcher.http.client.config.port}")int port) {
        return new OfferFetcherRestTemplate(restTemplate, uri, port);
    }

}
