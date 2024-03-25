package joboffers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import joboffers.infrastructure.token.controller.JwtResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JobOffersApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class BaseIntegrationTest {

    public static final String WIRE_MOCK_HOST = "http://localhost";
    public static final String FULL_IMAGE_NAME = "redis";

    public static final String REDIS_TIME_TO_LIVE = "PT1S";



    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    protected String registerAndGetToken() throws Exception {
        //register user process with fetch token
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "someUser",
                                "password": "somePassword"
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
        return jwtResponseDto.token();
    }

}
