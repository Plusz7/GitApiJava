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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
@RunWith(SpringRunner.class)
public class GitHubRepositoryIntegrationTest {

    private static final String GET_BODY_RESPONSE = """
        [
            {
                "id": 1,
                "node_id": "node1",
                "name": "Repo1",
                "full_name": "User1/Repo1",
                "owner": {
                    "login": "User1",
                    "url": "https://github.com/User1"
                },
                "private": false,
                "html_url": "https://github.com/User1/Repo1"
            },
            {
                "id": 2,
                "node_id": "node2",
                "name": "Repo2",
                "full_name": "User1/Repo2",
                "owner": {
                    "login": "User1",
                    "url": "https://github.com/User1"
                },
                "private": true,
                "html_url": "https://github.com/User1/Repo2"
            }
        ]
    """;

    @Autowired
    private GithubRepository githubRepository;

    @Autowired
    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() {
        mockWebServer.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(GET_BODY_RESPONSE).setResponseCode(200));
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
