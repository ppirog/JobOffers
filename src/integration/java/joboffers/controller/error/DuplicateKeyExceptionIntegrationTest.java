package joboffers.controller.error;

import joboffers.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WithMockUser
class DuplicateKeyExceptionIntegrationTest extends BaseIntegrationTest {
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @Container
    public static final GenericContainer<?> REDIS;

    static {

        REDIS = new GenericContainer<>(FULL_IMAGE_NAME)
                .withExposedPorts(6379);
        REDIS.start();
    }
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
    public void should_throw_and_handle_mongo_write_exception_when_url_are_not_unique() throws Exception {
        final ResultActions perform1 = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "string",
                  "companyName": "string",
                  "salary": "5000",
                  "url": "url"
                }
                """));

        perform1.andExpect(status().isCreated()).andReturn();

        final ResultActions perform2 = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "string",
                  "companyName": "string",
                  "salary": "5000",
                  "url": "url"
                }
                """));

        perform2.andExpect(status().isConflict()).andReturn();
    }
}
