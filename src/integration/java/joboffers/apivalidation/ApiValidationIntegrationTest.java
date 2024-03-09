package joboffers.apivalidation;

import joboffers.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiValidationIntegrationTest extends BaseIntegrationTest {
    @Test
    void should_throw_bad_request_to_user_when_url_are_null() throws Exception {

        final ResultActions perform = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {}
                """));
        perform.andExpect(status().isBadRequest());
        perform.andExpect(content().json("""
                                {
                                    "errors": [
                                            "url cannot be empty",
                                            "url cannot be null",
                                            "salary cannot be empty",
                                            "salary cannot be null",
                                            "companyName cannot be empty",
                                            "companyName cannot be null",
                                            "jobTitle cannot be empty",
                                            "jobTitle cannot be null"
                                        ],
                                    "status" : "BAD_REQUEST"
                                }
                """.trim()
        ));
    }

    @Test
    void should_throw_bad_request_to_user_when_url_are_empty() throws Exception {

        final ResultActions perform = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "string",
                  "companyName": "string",
                  "salary": "1000",
                  "url": ""
                }
                """));
        perform.andExpect(status().isBadRequest());
        perform.andExpect(content().json("""
                                {
                                    "errors": [
                                            "url cannot be empty"
                                        ],
                                    "status" : "BAD_REQUEST"
                                }
                """.trim()
        ));
    }

    @Test
    void should_throw_bad_request_to_user_when_salary_are_empty() throws Exception {

        final ResultActions perform = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "string",
                  "companyName": "string",
                  "salary": "",
                  "url": "1"
                }
                """));
        perform.andExpect(status().isBadRequest());
        perform.andExpect(content().json("""
                                {
                                    "errors": [
                                            "salary cannot be empty"
                                        ],
                                    "status" : "BAD_REQUEST"
                                }
                """.trim()
        ));
    }

    @Test
    void should_throw_bad_request_to_user_when_companyName_are_empty() throws Exception {

        final ResultActions perform = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "string",
                  "companyName": "",
                  "salary": "5000",
                  "url": "1"
                }
                """));
        perform.andExpect(status().isBadRequest());
        perform.andExpect(content().json("""
                                {
                                    "errors": [
                                            "companyName cannot be empty"
                                        ],
                                    "status" : "BAD_REQUEST"
                                }
                """.trim()
        ));
    }

    @Test
    void should_throw_bad_request_to_user_when_jobTitle_are_empty() throws Exception {

        final ResultActions perform = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "jobTitle": "",
                  "companyName": "string",
                  "salary": "5000",
                  "url": "1"
                }
                """));
        perform.andExpect(status().isBadRequest());
        perform.andExpect(content().json("""
                                {
                                    "errors": [
                                            "jobTitle cannot be empty"
                                        ],
                                    "status" : "BAD_REQUEST"
                                }
                """.trim()
        ));
    }
}
