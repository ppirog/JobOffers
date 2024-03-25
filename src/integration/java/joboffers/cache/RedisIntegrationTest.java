package joboffers.cache;

import com.github.tomakehurst.wiremock.client.WireMock;
import joboffers.BaseIntegrationTest;
import joboffers.SampleOffersResponse;
import joboffers.domain.offer.OfferFacade;
import joboffers.infrastructure.token.controller.JwtResponseDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class RedisIntegrationTest extends BaseIntegrationTest implements SampleOffersResponse {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @Container
    private static final GenericContainer<?> REDIS;


    static {

        REDIS = new GenericContainer<>(FULL_IMAGE_NAME)
                .withExposedPorts(6379);
        REDIS.start();
    }

    @SpyBean
    OfferFacade offerFacade;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("job-offers.offer-fetcher.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("job-offers.offer-fetcher.http.client.config.port", wireMockServer::getPort);
        registry.add("spring.cache.redis.time-to-live", () -> REDIS_TIME_TO_LIVE);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.redis", () -> FULL_IMAGE_NAME);
    }

    @Test
    public void should_cached_all_offers_using_redis() throws Exception {

        wireMockServer.stubFor(
                WireMock.get("/offers").willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", "application/json").withBody(
                        getSampleOffersResponse2Offers()
                )));

        //register user process with fetch token
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "someUser",
                                "password": "somePassword"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                        "message": "User registered",
                        "status": "CREATED"
                        }
                        """));


        final ResultActions perform5 = mockMvc.perform(post("/token").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "username": "someUser",
                "password": "somePassword"
                }
                """));

        final MvcResult mvcResult4 = perform5.andExpect(status().isOk()).andReturn();
        final JwtResponseDto jwtResponseDto = objectMapper.readValue(mvcResult4.getResponse().getContentAsString(), JwtResponseDto.class);
        String token = jwtResponseDto.token();

        //step 1: user tried to GET /offers 15 times and only 3 times findOffOffers method will be done. The rest of responses will be taken from redis cache
        await()
                .atMost(Duration.ofSeconds(3))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() -> {
                            mockMvc.perform(get("/offers")
                                    .header("Authorization", "Bearer " + token)
                            ).andExpect(status().isOk()).andReturn();

                            verify(offerFacade, times(3)).findAllOffers();
                        }
                );
    }
}