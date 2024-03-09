package joboffers.controller.error;

import joboffers.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DuplicateKeyExceptionIntegrationTest extends BaseIntegrationTest {

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
