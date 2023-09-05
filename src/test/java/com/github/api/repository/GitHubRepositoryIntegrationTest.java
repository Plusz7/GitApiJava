package com.github.api.repository;

import com.github.api.config.TestConfig;
import com.github.api.model.dto.UserRepositoryDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
@RunWith(SpringRunner.class)
public class GitHubRepositoryIntegrationTest {

    private static final String RESPONSE_BODY = "[" +
            "{" +
            "'id': 1349611," +
            "'node_id': 'MDEwOlJlcG9zaXRvcnkxMzQ5NjEx'," +
            "'name': 'CustomField'," +
            "'full_name': 'somename/CustomField'," +
            "'owner': {" +
            "'login': 'somename'," +
            "'url': 'https://api.github.com/users/somename'" +
            "}," +
            "'html_url': 'https://github.com/somename/CustomField'" +
            // Add any other necessary fields from UserRepositoryDto here...
            "}" +
            "]";

    @Autowired
    private GithubRepository githubRepository;

    @Autowired
    private MockWebServer mockWebServer;

    @Autowired
    private WebClient webClient;

    @BeforeEach
    public void setup() {
        mockWebServer.enqueue(new MockResponse().setBody(RESPONSE_BODY).setResponseCode(200));
    }

    @AfterEach
    public void teardown() throws IOException{
        mockWebServer.shutdown();
    }

    @Test
    public void fetchUserRepos_ShouldReturnCorrectRepos() {
        List<UserRepositoryDto> repos = githubRepository.getRepositoryFromUser("somename");
        assertThat(repos).hasSize(1);
    }
}
