package com.englishacademy.course;

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
 * Integration test covering course + lesson creation and retrieval against a
 * real MySQL instance (Testcontainers) with Flyway migrations applied.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CourseControllerIT {

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
    private CourseRepository courseRepository;

    @BeforeEach
    void resetState() {
        courseRepository.deleteAll();
    }

    @Test
    void createsCourseAddsLessonAndReturnsItInDetail() {
        Map<String, Object> coursePayload = Map.of(
                "title", "Business English",
                "description", "Workplace communication for professionals",
                "level", "B1",
                "durationHours", 20
        );

        ResponseEntity<Map<String, Object>> createResponse = restTemplate.exchange(
                "/api/courses", HttpMethod.POST, new HttpEntity<>(coursePayload),
                new ParameterizedTypeReference<>() {
                });

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number courseId = (Number) createResponse.getBody().get("id");

        Map<String, Object> lessonPayload = Map.of(
                "title", "Present Simple",
                "content", "Used for habits and general truths.",
                "orderIndex", 0
        );
        ResponseEntity<Map<String, Object>> lessonResponse = restTemplate.exchange(
                "/api/courses/" + courseId + "/lessons", HttpMethod.POST, new HttpEntity<>(lessonPayload),
                new ParameterizedTypeReference<>() {
                });
        assertThat(lessonResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Map<String, Object>> detailResponse = restTemplate.exchange(
                "/api/courses/" + courseId, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(detailResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(detailResponse.getBody().get("title")).isEqualTo("Business English");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> lessons = (List<Map<String, Object>>) detailResponse.getBody().get("lessons");
        assertThat(lessons).hasSize(1);
        assertThat(lessons.get(0).get("title")).isEqualTo("Present Simple");
    }

    @Test
    void returnsProblemDetailWhenCourseNotFound() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/courses/999999", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("title")).isEqualTo("Resource not found");
    }
}
