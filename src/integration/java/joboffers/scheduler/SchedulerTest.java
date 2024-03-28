package joboffers.scheduler;

import joboffers.BaseIntegrationTest;
import joboffers.domain.offer.OfferFetchable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@WithMockUser
public class SchedulerTest extends BaseIntegrationTest {
    @Container
    public static final GenericContainer<?> REDIS;

    static {

        REDIS = new GenericContainer<>(FULL_IMAGE_NAME)
                .withExposedPorts(6379);
        REDIS.start();
    }
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("job-offers.offer-fetcher.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("job-offers.offer-fetcher.http.client.config.port", wireMockServer::getPort);

        registry.add("spring.cache.redis.time-to-live", () -> REDIS_TIME_TO_LIVE);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.redis", () -> FULL_IMAGE_NAME);
    }



    @SpyBean
    OfferFetchable offerFetcher;

    @Test
    public void should_run_http(){
        await()
                .atMost(java.time.Duration.ofSeconds(5))
                .untilAsserted(() -> verify(offerFetcher, times(5)).fetchAllOffers());
    }
}
