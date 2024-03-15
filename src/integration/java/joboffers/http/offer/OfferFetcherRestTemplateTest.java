package joboffers.http.offer;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import joboffers.SampleOffersResponse;
import joboffers.domain.offer.OfferFetchable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class OfferFetcherRestTemplateTest extends OfferFetcherRestTemplateTestConfig implements SampleOffersResponse {


    OfferFetchable offerFetchable = remoteOfferFetcherClient(restTemplateResponseErrorHandler());

    private final String CONTENT_TYPE = "Content-Type";
    private final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

    @Test
    void should_throw_exception_when_fault_CONNECTION_RESET_BY_PEER() {

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE)
                        .withBody(getSampleOffersResponse2Offers())
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                )
        );

        Throwable throwable = catchThrowable(() -> offerFetchable.fetchAllOffers());

        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResponseStatusException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );
    }

    @Test
    void should_throw_exception_when_fault_EMPTY_RESPONSE() {

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE)
                        .withBody(getSampleOffersResponse2Offers())
                        .withFault(Fault.EMPTY_RESPONSE)
                )
        );

        Throwable throwable = catchThrowable(() -> offerFetchable.fetchAllOffers());

        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResponseStatusException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );
    }

    @Test
    void should_throw_exception_when_fault_MALFORMED_RESPONSE_CHUNK() {

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE)
                        .withBody(getSampleOffersResponse2Offers())
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                )
        );

        Throwable throwable = catchThrowable(() -> offerFetchable.fetchAllOffers());

        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResponseStatusException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );
    }

    @Test
    void should_throw_exception_when_fault_RANDOM_DATA_THEN_CLOSE() {

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE)
                        .withBody(getSampleOffersResponse2Offers())
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                )
        );

        Throwable throwable = catchThrowable(() -> offerFetchable.fetchAllOffers());


        //ZAMIAST RESPONSESTATUSEXCEPTINO ILLEGALARGUMENTEXCEPTION
        assertAll(
                () -> assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
        );

    }

}
