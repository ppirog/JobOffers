package joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import joboffers.BaseIntegrationTest;
import joboffers.SampleOffersResponse;
import joboffers.domain.offer.OfferFacade;
import joboffers.domain.offer.dto.OfferResponseDto;
import joboffers.infrastructure.offer.controller.dto.UserResponseDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;

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

        //        step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        //given

        wireMockServer.stubFor(
                WireMock.get("/offers").willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", "application/json").withBody(
                        getSampleOffersResponse0Offers()
                )));

        //when
        //then
        assertEquals(0, offerFacade.findAllOffers().size());
        assertEquals(0, offerFacade.fetchAllOffersAndSaveAllIfNotExist().size());
        assertEquals(0, offerFacade.findAllOffers().size());
        //step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        //given

        await()
                .pollInterval(Duration.ofSeconds(1))
                .atMost(Duration.ofSeconds(3))
                .until(() -> {
                            try {
                                offerFacade.fetchAllOffersAndSaveAllIfNotExist();
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                );

        //when


        //step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers 7
        //given
        final ResultActions perform = mockMvc.perform(get("/offers"));

        final MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();

        final UserResponseDto userResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserResponseDto.class);
        System.out.println("\n\n\n\n\n");
        log.info("userResponseDto: {}", userResponseDto);

        assertAll(
                () -> assertEquals(0, userResponseDto.offers().size()),
                () -> assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value())
        );

        //step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”

        final ResultActions perform1 = mockMvc.perform(get("/offers/9999"));

        perform1.andExpect(content().json("""
                                {
                                    "message" : "Offer with id: 9999 not found",
                                    "status" : "NOT_FOUND"
                                }
                """
        ));

        //step 16: user made POST /offers with header
        final ResultActions perform3 = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
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
        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        final ResultActions perform4 = mockMvc.perform(get("/offers/"+ offerResponseDto.id()));
        perform4.andExpect(status().isOk());

        final MvcResult mvcResult2 = mockMvc.perform(get("/offers")).andExpect(status().isOk()).andReturn();
        final UserResponseDto userResponseDto1 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), UserResponseDto.class);

        assertEquals(1, userResponseDto1.offers().size());
    }
}
