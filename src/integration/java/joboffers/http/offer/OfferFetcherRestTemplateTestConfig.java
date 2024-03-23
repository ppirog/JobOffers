package joboffers.http.offer;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import joboffers.domain.offer.OfferFetchable;
import joboffers.infrastructure.offer.http.OfferFetcherClientConfig;
import joboffers.infrastructure.offer.http.OfferFetcherRestTemplate;
import joboffers.infrastructure.offer.http.RestTemplateResponseErrorHandler;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class OfferFetcherRestTemplateTestConfig extends OfferFetcherClientConfig {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    public OfferFetchable remoteOfferFetcherClient(RestTemplateResponseErrorHandler handler) {
        final RestTemplate restTemplate = restTemplate(handler);
        return new OfferFetcherRestTemplate(restTemplate, "http://localhost", wireMockServer.getPort());
    }

}
