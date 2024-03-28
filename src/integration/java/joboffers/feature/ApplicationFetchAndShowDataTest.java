package joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import joboffers.BaseIntegrationTest;
import joboffers.SampleOffersResponse;
import joboffers.domain.offer.OfferFacade;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.infrastructure.offer.controller.dto.UserResponseDto;
import joboffers.infrastructure.token.controller.JwtResponseDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class ApplicationFetchAndShowDataTest extends BaseIntegrationTest implements SampleOffersResponse {

    @Autowired
    OfferFacade offerFacade;

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

    @Test
    public void user_want_to_see_offers_but_have_to_logged_in_and_external_server_should_have_some_offers() throws Exception {

        /*
        step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers

        step 8: there are 2 new offers in external HTTP server
        step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000

        step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        step 12: user made GET /offers/1000 and system returned OK(200) with offer
        step 13: there are 2 new offers in external HTTP server
        step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000

        step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
        step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        */



        //step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        wireMockServer.stubFor(
                WireMock.get("/offers").willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", "application/json").withBody(
                        getSampleOffersResponse0Offers()
                )));


        assertEquals(0, offerFacade.findAllOffers().size());
        assertEquals(0, offerFacade.fetchNewOffersAndSaveToDatabase().size());
        assertEquals(0, offerFacade.findAllOffers().size());



        //step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        await()
                .pollInterval(Duration.ofSeconds(1))
                .atMost(Duration.ofSeconds(3))
                .until(() -> {
                            try {
                                offerFacade.fetchNewOffersAndSaveToDatabase();
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                );



        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        final ResultActions perform2 = mockMvc
                .perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                "username": "someUser",
                "password": "somePassword"
                }
                """));

                perform2.andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                {
                "message": "Bad credentials",
                "status": "UNAUTHORIZED"
                }
                """));



        //step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        mockMvc.perform(get("/offers"))
                .andExpect(status().isUnauthorized());



        //step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status CREATED(201)
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



        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        final ResultActions perform5 = mockMvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """));

        final MvcResult mvcResult4 = perform5.andExpect(status().isOk()).andReturn();
        final JwtResponseDto jwtResponseDto = objectMapper.readValue(mvcResult4.getResponse().getContentAsString(), JwtResponseDto.class);
        String token = jwtResponseDto.token();

        assertAll(
                () -> assertEquals(jwtResponseDto.username(),"someUser"),
                () -> assertThat(token).matches(Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"))
        );



        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers 7
        final ResultActions perform = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
        );

        final MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();

        final UserResponseDto userResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponseDto.class);
        System.out.println("\n\n\n\n\n");
        log.info("userResponseDto: {}", userResponseDto);

        assertAll(
                () -> assertEquals(0, userResponseDto.offers().size()),
                () -> assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value())
        );

        assertEquals(0, offerFacade.findAllOffers().size());



        //step 8 there are 2 new offers in external HTTP server
        wireMockServer.stubFor(
                WireMock.get("/offers").willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(getSampleOffersResponse2Offers()
                        )));



        //step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        await()
                .pollInterval(Duration.ofSeconds(1))
                .atMost(Duration.ofSeconds(3))
                .until(() -> {
                            try {
                                offerFacade.fetchNewOffersAndSaveToDatabase();
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                );



        //step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers
        final MvcResult mvcResult3 = mockMvc.perform(get("/offers")
                        .header("Authorization","Bearer " + token)
                )
                .andExpect(status().isOk()).andReturn();

        final UserResponseDto userResponseDto2 = objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), UserResponseDto.class);
        final List<OfferResponseDto> dtos = userResponseDto2.offers();
        dtos.sort(Comparator.comparing(OfferResponseDto::jobTitle));
        assertEquals(2, dtos.size());

        mockMvc.perform(get("/offers/" + dtos.get(0).id())
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/offers/" + dtos.get(1).id())
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());

        assertAll(
                () -> assertEquals("AJunior Java Developer", dtos.get(0).jobTitle()),
                () -> assertEquals("BJava (CMS) Developer", dtos.get(1).jobTitle()),

                () -> assertEquals("BlueSoft Sp. z o.o.", dtos.get(0).companyName()),
                () -> assertEquals("Efigence SA", dtos.get(1).companyName()),

                () -> assertEquals("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre", dtos.get(0).url()),
                () -> assertEquals("https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh", dtos.get(1).url()),

                () -> assertEquals("7 000 - 9 000 PLN", dtos.get(0).salary()),
                () -> assertEquals("16 000 - 18 000 PLN", dtos.get(1).salary())
        );



        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        mockMvc.perform(get("/offers/9999")
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNotFound());



        //step 12: user made GET /offers/1000 and system returned OK(200) with offer
        mockMvc.perform(get("/offers/" + dtos.get(0).id())
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/offers/" + dtos.get(1).id())
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());



        //step 13: there are 2 new offers in external HTTP server
        //step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        final ResultActions perform1 = mockMvc.perform(get("/offers/9999")
                .header("Authorization","Bearer " + token)
        );

        perform1.andExpect(content().json("""
                                {
                                    "message" : "Offer with id: 9999 not found",
                                    "status" : "NOT_FOUND"
                                }
                """
        ));



        //step 16: user made POST /offers with header
        final ResultActions perform3 = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                          "jobTitle": "string",
                          "companyName": "string",
                          "salary": "5000",
                          "url": "url"
                        }
                        """));

        final MvcResult mvcResult1 = perform3.andExpect(status().isCreated()).andReturn();
        final String contentAsString = mvcResult1.getResponse().getContentAsString();
        final OfferResponseDto offerResponseDto = objectMapper.readValue(contentAsString, OfferResponseDto.class);
        log.info("offerResponseDto: {}", offerResponseDto);

        assertAll(
                () -> assertEquals("url", offerResponseDto.url()),
                () -> assertEquals("string", offerResponseDto.jobTitle()),
                () -> assertEquals("string", offerResponseDto.companyName()),
                () -> assertEquals("5000", offerResponseDto.salary()),
                () -> assertThat(offerResponseDto.id()).isNotNull()
        );



        //step 17:user made GET /offers with header “Authorization Bearer AAAA.BBBB.CCC”and system returned OK (200) with 1 offer
        final ResultActions perform4 = mockMvc.perform(get("/offers/" + offerResponseDto.id())
                .header("Authorization", "Bearer " + token)
        );
        perform4.andExpect(status().isOk());

        final MvcResult mvcResult2 = mockMvc.perform(get("/offers")
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk()).andReturn();
        final UserResponseDto userResponseDto1 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), UserResponseDto.class);

        assertEquals(3, userResponseDto1.offers().size());
    }
}
