package com.englishacademy.studymodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test covering study module creation and retrieval against a real
 * MySQL instance. This specifically guards against a lazy-initialization bug:
 * the `topics` element collection is LAZY, and a unit test with a mocked
 * repository can never catch a DTO holding onto an uninitialized Hibernate
 * collection proxy that only blows up once Jackson serializes it after the
 * transaction (and session) has already closed.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class StudyModuleControllerIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudyModuleRepository studyModuleRepository;

    @BeforeEach
    void resetState() {
        studyModuleRepository.deleteAll();
    }

    @Test
    void createsModuleAndReturnsTopicsInOrderOnListEndpoint() {
        Map<String, Object> payload = Map.of(
                "title", "Grammar Bootcamp",
                "description", "desc",
                "level", "A2",
                "topics", List.of("Present tense", "Past tense", "Articles")
        );

        ResponseEntity<Map<String, Object>> createResponse = restTemplate.exchange(
                "/api/study-modules", HttpMethod.POST, new HttpEntity<>(payload),
                new ParameterizedTypeReference<>() {
                });
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // GET (list) is what previously failed: serialization happens after the
        // transaction closes, so this is the endpoint that must be exercised.
        ResponseEntity<List<Map<String, Object>>> listResponse = restTemplate.exchange(
                "/api/study-modules", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(1);
        @SuppressWarnings("unchecked")
        List<String> topics = (List<String>) listResponse.getBody().get(0).get("topics");
        assertThat(topics).containsExactly("Present tense", "Past tense", "Articles");
    }
}
