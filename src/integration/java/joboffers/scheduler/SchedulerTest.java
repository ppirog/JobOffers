package joboffers.scheduler;

import joboffers.BaseIntegrationTest;
import joboffers.domain.offer.OfferFetchable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetchable offerFetcher;

    @Test
    public void should_run_http(){
        await()
                .atMost(java.time.Duration.ofSeconds(5))
                .untilAsserted(() -> verify(offerFetcher, times(5)).fetchAllOffers());
    }
}
